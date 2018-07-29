package io.trane.ndbc.netty4;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.ssl.SslHandler;
import io.trane.future.Future;
import io.trane.future.Promise;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.ClientMessage;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.Try;

final public class NettyChannel extends SimpleChannelInboundHandler<Try<Optional<ServerMessage>>> implements Channel {

  private Promise<ChannelHandlerContext> ctx = Promise.apply();
  private final AtomicReference<Promise<ServerMessage>> nextMessagePromise = new AtomicReference<>(
      null);

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
  protected final void channelRead0(final ChannelHandlerContext ctx, final Try<Optional<ServerMessage>> msg)
      throws Exception {
    System.out.println(hashCode() + " received: " + msg);
    final Promise<ServerMessage> p = nextMessagePromise.get();
    if (p == null) {
      // throw new IllegalStateException("Unexpected server message: " + msg);
      // TODO logging
      return;
    }
    if (!nextMessagePromise.compareAndSet(p, null))
      p.setException(new IllegalStateException(
          "Invalid `nextMessagePromise` state: " + nextMessagePromise.get()));
    msg.ifSuccess(m -> m.ifPresent(p::setValue)).ifFailure(p::setException);
  }

  public final Future<Void> addSSLHandler(final SslHandler h) {
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
    System.out.println(hashCode() + " sent: " + msg);
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

  public Future<ChannelHandlerContext> ctx() {
    return ctx;
  }
}
