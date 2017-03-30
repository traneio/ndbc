package io.trane.ndbc.postgres.netty4.decoder;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.trane.ndbc.postgres.proto.Message;
import io.trane.ndbc.postgres.proto.Message.BackendKeyData;
import io.trane.ndbc.postgres.proto.Message.BindComplete;
import io.trane.ndbc.postgres.proto.Message.CloseComplete;

public class Decoder extends ByteToMessageDecoder {

  private final AuthenticationRequestDecoder authenticationRequestDecoder;
  private final CommandCompleteDecoder commandCompleteDecoder;

  private final BindComplete bindComplete = new BindComplete();
  private final CloseComplete closeComplete = new CloseComplete();

  public Decoder(Charset charset) {
    super();
    this.authenticationRequestDecoder = new AuthenticationRequestDecoder();
    this.commandCompleteDecoder = new CommandCompleteDecoder(charset);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
    if (buf.readableBytes() >= 5) {
      buf.markReaderIndex();
      byte tpe = buf.readByte();
      int length = buf.readInt() - 4;
      if (length < 0)
        throw new RuntimeException("Invalid message length:" + length);
      if (buf.readableBytes() >= length)
        out.add(decode(tpe, buf.readSlice(length)));
    }
  }

  private final Message decode(byte tpe, ByteBuf buf) {
    switch (tpe) {

    case 'R':
      return authenticationRequestDecoder.decode(buf);
    case 'K':
      return new BackendKeyData(buf.readInt(), buf.readInt());
    case '2':
      return bindComplete;
    case '3':
      return closeComplete;
    case 'C':
      return commandCompleteDecoder.decode(buf);
    }
    return null;
  }
}
