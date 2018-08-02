package io.trane.ndbc.netty4;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.ssl.SslHandler;
import io.trane.future.Future;
import io.trane.future.Promise;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.ClientMessage;
import io.trane.ndbc.proto.Marshaller;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.proto.Unmarshaller;
import io.trane.ndbc.util.NonFatalException;

final public class NettyChannel extends SimpleChannelInboundHandler<BufferReader> implements Channel {

  private final Charset                                 charset;
  private Promise<ChannelHandlerContext>                ctx                 = Promise.apply();
  private final AtomicReference<Consumer<BufferReader>> nextMessageConsumer = new AtomicReference<>(null);

  public NettyChannel(Charset charset) {
    this.charset = charset;
  }

  @Override
  public <T extends ClientMessage> Future<Void> send(Marshaller<T> marshaller, T msg) {
    System.out.println(hashCode() + " sent: " + msg);
    return ctx.flatMap(c -> {
      final ByteBuf ioBuffer = c.alloc().ioBuffer();
      marshaller.apply(msg, new BufferWriter(charset, ioBuffer));
      c.write(ioBuffer);
      return Future.VOID;
    });
  }

  @Override
  public <T extends ServerMessage> Future<T> receive(Unmarshaller<T> unmarshaller) {
    return ctx.flatMap(c -> {
      System.out.println(hashCode() + " requested: " + unmarshaller);
      final Promise<T> p = Promise.apply();
      Consumer<BufferReader> consumer = (b) -> {
        try {
          final T msg = unmarshaller.apply(b);
          System.out.println(hashCode() + " received: " + msg);
          b.release();
          p.setValue(msg);
        } catch (Throwable ex) {
          NonFatalException.verify(ex);
          p.setException(ex);
        }
      };
      if (nextMessageConsumer.compareAndSet(null, consumer)) {
        c.flush();
        c.read();
        return p;
      } else
        return Future.exception(new IllegalStateException("Previous `receive` still pending."));
    });
  }

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
  public final void channelRead0(final ChannelHandlerContext ctx, final BufferReader msg) {
    nextMessageConsumer.getAndSet(null).accept(msg);
  }

  public final Future<Void> addSSLHandler(final SslHandler h) {
    return ctx.onSuccess(c -> c.pipeline().addFirst(h)).voided();
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
