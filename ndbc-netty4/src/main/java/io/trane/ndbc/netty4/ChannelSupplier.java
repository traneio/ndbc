package io.trane.ndbc.netty4;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.flow.FlowControlHandler;
import io.trane.future.Future;
import io.trane.future.Promise;

public final class ChannelSupplier implements Supplier<Future<NettyChannel>> {

  private final EventLoopGroup eventLoopGroup;
  private final String         host;
  private final int            port;
  private final Charset        charset;

  private final Function<io.trane.ndbc.proto.BufferReader, Optional<io.trane.ndbc.proto.BufferReader>> transformBufferReader;

  public ChannelSupplier(final EventLoopGroup eventLoopGroup, final String host, final int port,
      final Charset charset,
      Function<io.trane.ndbc.proto.BufferReader, Optional<io.trane.ndbc.proto.BufferReader>> transformBufferReader) {
    this.eventLoopGroup = eventLoopGroup;
    this.host = host;
    this.port = port;
    this.charset = charset;
    this.transformBufferReader = transformBufferReader;
  }

  @Override
  public final Future<NettyChannel> get() {
    final NettyChannel channel = new NettyChannel(charset);
    return bootstrap(channel).map(v -> channel);
  }

  private final ByteToMessageDecoder toBufferReaderDecoder() {
    return new ByteToMessageDecoder() {
      @Override
      protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out)
          throws Exception {
        transformBufferReader.apply(new BufferReader(in)).ifPresent(out::add);
      }
    };
  }

  private final Future<Void> bootstrap(final NettyChannel channel) {
    final Promise<Void> p = Promise.apply();
    new Bootstrap()
        .group(eventLoopGroup)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.AUTO_READ, false)
        .handler(new ChannelInitializer<io.netty.channel.Channel>() {
          @Override
          protected void initChannel(final io.netty.channel.Channel ch) throws Exception {
            ch.pipeline().addLast(toBufferReaderDecoder(), new FlowControlHandler(), channel);
          }
        })
        .connect(new InetSocketAddress(host, port))
        .addListener(future -> p.become(Future.VOID));
    return p;
  }
}
