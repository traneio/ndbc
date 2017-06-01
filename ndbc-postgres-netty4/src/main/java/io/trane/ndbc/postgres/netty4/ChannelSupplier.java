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
import io.netty.handler.flow.FlowControlHandler;
import io.trane.future.Future;
import io.trane.future.Promise;
import io.trane.ndbc.postgres.proto.parser.Parser;
import io.trane.ndbc.postgres.proto.serializer.Serializer;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.ClientMessage;

public final class ChannelSupplier implements Supplier<Future<Channel>> {

  private final Serializer encoder;
  private final Parser decoder;
  private final EventLoopGroup eventLoopGroup;
  private final String host;
  private final int port;
  private final Charset charset;

  public ChannelSupplier(final Charset charset, final Serializer encoder, final Parser decoder,
      final EventLoopGroup eventLoopGroup, final String host, final int port) {
    super();
    this.charset = charset;
    this.encoder = encoder;
    this.decoder = decoder;
    this.eventLoopGroup = eventLoopGroup;
    this.host = host;
    this.port = port;
  }

  @Override
  public final Future<Channel> get() {
    final NettyChannel channel = new NettyChannel();
    return bootstrap(channel).map(v -> channel);
  }

  private final Future<Void> bootstrap(final NettyChannel channel) {
    final Promise<Void> p = Promise.apply();
    new Bootstrap().group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.AUTO_READ, false).handler(new ChannelInitializer<io.netty.channel.Channel>() {
          @Override
          protected void initChannel(final io.netty.channel.Channel ch) throws Exception {
            ch.pipeline().addLast(new ByteToMessageDecoder() {
              @Override
              protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out)
                  throws Exception {
                decoder.decode(new BufferReader(charset, in)).ifPresent(out::add);
              }
            }, new MessageToByteEncoder<ClientMessage>() {
              @Override
              protected void encode(final ChannelHandlerContext ctx, final ClientMessage msg, final ByteBuf out)
                  throws Exception {
                encoder.encode(msg, new BufferWriter(charset, out));
              }
            }, new FlowControlHandler(), channel);
          }
        }).connect(new InetSocketAddress(host, port)).addListener(future -> p.become(Future.VOID));
    return p;
  }

}
