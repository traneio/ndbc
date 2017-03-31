package io.trane.ndbc.postgres.netty4.decoder;

import java.nio.charset.Charset;
import java.util.List;
import static io.trane.ndbc.postgres.netty4.decoder.Read.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.trane.ndbc.postgres.proto.Message;
import io.trane.ndbc.postgres.proto.Message.*;
import io.trane.ndbc.postgres.proto.Message.BindComplete;
import io.trane.ndbc.postgres.proto.Message.CloseComplete;

public class Decoder extends ByteToMessageDecoder {

  private final Charset charset;

  private final AuthenticationRequestDecoder authenticationRequestDecoder;
  private final CommandCompleteDecoder commandCompleteDecoder;
  private final DataRowDecoder dataRowDecoder;
  private final InfoResponseFieldsDecoder infoResponseFieldsDecoder;

  private final BindComplete bindComplete = new BindComplete();
  private final CloseComplete closeComplete = new CloseComplete();
  private final CopyDone copyDone = new CopyDone();
  private final EmptyQueryResponse emptyQueryResponse = new EmptyQueryResponse();
  private final NoData noData = new NoData();

  public Decoder(Charset charset) {
    super();
    this.charset = charset;
    this.authenticationRequestDecoder = new AuthenticationRequestDecoder();
    this.commandCompleteDecoder = new CommandCompleteDecoder(charset);
    this.dataRowDecoder = new DataRowDecoder();
    this.infoResponseFieldsDecoder = new InfoResponseFieldsDecoder(charset);
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
    case 'd':
      return new CopyData(bytes(buf));
    case 'c':
      return copyDone;
    case 'G':
      return notImplemented(CopyInResponse.class);
    case 'H':
      return notImplemented(CopyOutResponse.class);
    case 'W':
      return notImplemented(CopyBothResponse.class);
    case 'D':
      return dataRowDecoder.decode(buf);
    case 'I':
      return emptyQueryResponse;
    case 'B':
      return new InfoResponse.ErrorResponse(infoResponseFieldsDecoder.decode(buf));
    case 'V':
      return notImplemented(FunctionCallResponse.class);
    case 'n':
      return noData;
    case 'N':
      return new InfoResponse.NoticeResponse(infoResponseFieldsDecoder.decode(buf));
    }
    return null;
  }

  private Message notImplemented(Class<?> cls) {
    throw new UnsupportedOperationException("Decoder not implemented for class: " + cls);
  }
}
