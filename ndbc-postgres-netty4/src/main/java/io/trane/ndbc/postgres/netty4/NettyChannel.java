package io.trane.ndbc.postgres.netty4;

import java.util.concurrent.atomic.AtomicReference;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.ssl.SslHandler;
import io.trane.future.Future;
import io.trane.future.Promise;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.ClientMessage;
import io.trane.ndbc.proto.ServerMessage;

final class NettyChannel extends SimpleChannelInboundHandler<ServerMessage> implements Channel {

  private Promise<ChannelHandlerContext>                ctx                = Promise.apply();
  private final AtomicReference<Promise<ServerMessage>> nextMessagePromise = new AtomicReference<>(null);

  @Override
  public final void channelActive(final ChannelHandlerContext ctx) throws Exception {
    this.ctx.setValue(ctx);
    super.channelActive(ctx);
  }

  @Override
  public final void channelInactive(final ChannelHandlerContext ctx) throws Exception {
    this.ctx = Promise.apply();
    this.ctx.setException(new IllegalStateException("Channel inactive."));
    super.channelInactive(ctx);
  }

  @Override
  protected final void channelRead0(final ChannelHandlerContext ctx, final ServerMessage msg) throws Exception {
    // System.out.println("received: " + msg);
    final Promise<ServerMessage> p = nextMessagePromise.get();
    if (p == null)
      throw new IllegalStateException("Unexpected server message: " + msg);
    if (!nextMessagePromise.compareAndSet(p, null))
      throw new IllegalStateException("Invalid `nextMessagePromise` state: " + nextMessagePromise.get());
    p.setValue(msg);
  }

  public final Future<Void> addSSLHandler(SslHandler h) {
    return ctx.onSuccess(c -> c.pipeline().addFirst(h)).voided();
  }

  @Override
  public final Future<ServerMessage> receive() {
    return ctx.flatMap(c -> {
      final Promise<ServerMessage> p = Promise.apply();
      if (nextMessagePromise.compareAndSet(null, p)) {
        c.flush();
        c.read();
        return p;
      } else
        return Future.exception(new IllegalStateException("Previous `receive` still pending."));
    });
  }

  @Override
  public final Future<Void> send(final ClientMessage msg) {
    // System.out.println("sent: " + msg);
    return ctx.flatMap(c -> {
      c.write(msg);// .addListener(future -> p.become(Future.VOID));
      return Future.VOID;
    });
  }

  @Override
  public final Future<Void> close() {
    return ctx.flatMap(c -> {
      final Promise<Void> p = Promise.apply();
      c.close().addListener(future -> {
        ctx = Promise.apply();
        ctx.setException(new IllegalStateException("Channel closed."));
        p.become(Future.VOID);
      });
      return p;
    });
  }
}
