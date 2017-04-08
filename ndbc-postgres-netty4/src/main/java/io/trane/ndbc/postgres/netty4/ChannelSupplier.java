package io.trane.ndbc.postgres.netty4;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Supplier;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.concurrent.GenericFutureListener;
import io.trane.future.Future;
import io.trane.future.Promise;
import io.trane.ndbc.postgres.proto.parser.Parser;
import io.trane.ndbc.postgres.proto.serializer.Serializer;
import io.trane.ndbc.proto.ClientMessage;

public class ChannelSupplier implements Supplier<Future<Channel>> {

  private final Serializer encoder;
  private final Parser decoder;
  private final EventLoopGroup eventLoopGroup;
  private final String host;
  private final int port;
  private final Charset charset;

  public ChannelSupplier(Charset charset, Serializer encoder, Parser decoder, EventLoopGroup eventLoopGroup, String host,
      int port) {
    super();
    this.charset = charset;
    this.encoder = encoder;
    this.decoder = decoder;
    this.eventLoopGroup = eventLoopGroup;
    this.host = host;
    this.port = port;
  }

  public final Future<Channel> get() {
    Channel channel = new Channel();
    return bootstrap(channel).map(v -> channel);
  }

  private final Future<Void> bootstrap(Channel channel) {
    Promise<Void> p = Promise.apply();
    new Bootstrap()
        .group(eventLoopGroup)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.AUTO_READ, false)
        .handler(new ChannelInitializer<io.netty.channel.Channel>() {
          @Override
          protected void initChannel(io.netty.channel.Channel ch) throws Exception {
            ch.pipeline().addLast(
                new ByteToMessageDecoder() {
                  @Override
                  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                    decoder.decode(new BufferReader(charset, in)).ifPresent(out::add);
                  }
                },
                new MessageToByteEncoder<ClientMessage>() {
                  @Override
                  protected void encode(ChannelHandlerContext ctx, ClientMessage msg, ByteBuf out) throws Exception {
                    encoder.encode(msg, new BufferWriter(charset, out));
                  }
                },
                channel);
          }
        })
        .connect(new InetSocketAddress(host, port))
        .addListener(new GenericFutureListener<io.netty.util.concurrent.Future<? super Void>>() {
          @Override
          public void operationComplete(io.netty.util.concurrent.Future<? super Void> future) throws Exception {
            p.become(Future.VOID);
          }
        });
    return p;
  }

}
