package io.trane.ndbc.postgres.netty4;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.GenericFutureListener;
import io.trane.future.Future;
import io.trane.future.Promise;
import io.trane.ndbc.proto.ClientMessage;
import io.trane.ndbc.proto.ServerMessage;

public class NettyChannel extends SimpleChannelInboundHandler<ServerMessage> implements io.trane.ndbc.proto.Channel {

  private Promise<ChannelHandlerContext> ctx = Promise.apply();
  private AtomicReference<Optional<Promise<ServerMessage>>> nextMessagePromise = new AtomicReference<>(
      Optional.empty());

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    this.ctx.setValue(ctx);
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    this.ctx = Promise.apply();
    this.ctx.setException(new IllegalStateException("Channel inactive."));
    super.channelInactive(ctx);
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ServerMessage msg) throws Exception {
    System.out.println("received: " + msg);
    Optional<Promise<ServerMessage>> option = nextMessagePromise.get();
    Promise<ServerMessage> p = option.orElseGet(() -> {
      throw new IllegalStateException("Unexpected server message: " + msg);
    });
    if (!nextMessagePromise.compareAndSet(option, Optional.empty()))
      throw new IllegalStateException("Invalid `nextMessagePromise` state: " + nextMessagePromise.get());
    p.setValue(msg);
  }

  @Override
  public Future<ServerMessage> receive() {
    return ctx.flatMap(c -> {
      Promise<ServerMessage> p = Promise.apply();
      if (nextMessagePromise.compareAndSet(Optional.empty(), Optional.of(p))) {
        c.read();
        return p;
      } else
        return Future.exception(new IllegalStateException("Previous `receive` still pending."));
    });
  }

  @Override
  public Future<Void> send(ClientMessage msg) {
    return ctx.flatMap(c -> {
      System.out.println("sent: " + msg);
      Promise<Void> p = Promise.apply();
      c.writeAndFlush(msg).addListener(new GenericFutureListener<io.netty.util.concurrent.Future<? super Void>>() {
        @Override
        public void operationComplete(io.netty.util.concurrent.Future<? super Void> future) throws Exception {
          p.become(Future.VOID);
        }
      });
      return p;
    });
  }

  @Override
  public Future<Void> close() {
    return ctx.flatMap(c -> {
      Promise<Void> p = Promise.apply();
      c.close().addListener(new GenericFutureListener<io.netty.util.concurrent.Future<? super Void>>() {
        @Override
        public void operationComplete(io.netty.util.concurrent.Future<? super Void> future) throws Exception {
          ctx = Promise.apply();
          ctx.setException(new IllegalStateException("Channel closed."));
          p.become(Future.VOID);
        }
      });
      return p;
    });
  }
}
