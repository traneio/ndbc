package io.trane.ndbc.netty4;

import java.nio.charset.Charset;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.trane.future.Future;
import io.trane.future.Promise;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.ClientMessage;
import io.trane.ndbc.proto.Marshaller;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.proto.Unmarshaller;
import io.trane.ndbc.util.NonFatalException;

final public class NettyChannel extends SimpleChannelInboundHandler<BufferReader> implements Channel {

  private static final Logger log = LoggerFactory.getLogger(NettyChannel.class);

  private final Charset                                 charset;
  private Promise<ChannelHandlerContext>                ctx                 = Promise.apply();
  private final AtomicReference<Consumer<BufferReader>> nextMessageConsumer = new AtomicReference<>(null);

  public NettyChannel(final Charset charset) {
    this.charset = charset;
  }

  @Override
  public <T extends ClientMessage> Future<Void> send(final Marshaller<T> marshaller, final T msg) {
    log.debug(channelId() + " sent: " + msg);
    return ctx.flatMap(c -> {
      final ByteBuf ioBuffer = c.alloc().ioBuffer();
      marshaller.apply(msg, new BufferWriter(charset, ioBuffer));
      ChannelFuture write = c.write(ioBuffer);
      c.flush();
      return ChannelFutureHandler.toFuture(write);
    });
  }

  private String channelId() {
    return Integer.toHexString(hashCode());
  }

  @Override
  public <T extends ServerMessage> Future<T> receive(final Unmarshaller<T> unmarshaller) {
    return ctx.flatMap(c -> {
      log.debug(channelId() + " requested: " + unmarshaller);
      final Promise<T> p = Promise.apply();
      final Consumer<BufferReader> consumer = new Consumer<BufferReader>() {
        @Override
        public void accept(final BufferReader b) {
          try {
            final Optional<T> option = unmarshaller.apply(b);
            if (b.readableBytes() > 0)
              throw new IllegalStateException("Bug - Unmarshaller " + unmarshaller + " didn't consume all bytes.");
            option.ifPresent(msg -> {
              log.debug(channelId() + " received: " + msg);
              b.release();
              p.setValue(msg);
            });
            if (!option.isPresent()) {
              if (!nextMessageConsumer.compareAndSet(null, this))
                throw new IllegalStateException("Previous `receive` still pending.");
              c.read();
            }
          } catch (final Throwable ex) {
            NonFatalException.verify(ex);
            p.setException(ex);
          }
        }
      };
      if (nextMessageConsumer.compareAndSet(null, consumer)) {
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
