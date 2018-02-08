package io.trane.ndbc.mysql.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.flow.FlowControlHandler;
import io.trane.future.Future;
import io.trane.future.Promise;
import io.trane.ndbc.mysql.proto.Message;
import io.trane.ndbc.netty4.BufferReader;
import io.trane.ndbc.netty4.BufferWriter;
import io.trane.ndbc.netty4.NettyChannel;
import io.trane.ndbc.mysql.proto.marshaller.Marshaller;
import io.trane.ndbc.mysql.proto.unmarshaller.Unmarshaller;

//
import static io.trane.ndbc.mysql.proto.Message.*;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Supplier;

final class ChannelSupplier implements Supplier<Future<NettyChannel>> {

  private final Marshaller     encoder;
  private final Unmarshaller   decoder;
  private final EventLoopGroup eventLoopGroup;
  private final String         host;
  private final int            port;
  private final Charset        charset;

  public ChannelSupplier(final Charset charset, final Marshaller encoder,
                         final Unmarshaller decoder,
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
  public final Future<NettyChannel> get() {
    final NettyChannel channel = new NettyChannel();
    return bootstrap(channel).map(v -> channel);
  }

  private class MessageCodec extends ByteToMessageCodec<Message> {
    ClientMessage previousClientMessage = new NoCommand();
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
      encoder.encode(msg, new BufferWriter(charset, out));
      this.previousClientMessage = (ClientMessage) msg;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
      decoder.decode(previousClientMessage, new BufferReader(charset, in)).ifPresent(out::add);
    }
  }

  private final Future<Void> bootstrap(final NettyChannel channel) {
    final Promise<Void> p = Promise.apply();
    new Bootstrap().group(eventLoopGroup).channel(NioSocketChannel.class)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.AUTO_READ, true)
        .handler(new ChannelInitializer<io.netty.channel.Channel>() {
          @Override
          protected void initChannel(final io.netty.channel.Channel ch) throws Exception {
            ch.pipeline().addLast(new MessageCodec(),
                new FlowControlHandler(), channel);
          }
        })
        .connect(new InetSocketAddress(host, port))
        .addListener(future -> p.become(Future.VOID));
    return p;
  }

}
