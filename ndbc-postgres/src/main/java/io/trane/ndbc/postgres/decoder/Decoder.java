package io.trane.ndbc.postgres.decoder;

import java.util.Optional;

import io.trane.ndbc.postgres.proto.Message;
import io.trane.ndbc.postgres.proto.Message.BackendKeyData;
import io.trane.ndbc.postgres.proto.Message.*;
import io.trane.ndbc.postgres.proto.Message.CloseComplete;
import io.trane.ndbc.postgres.proto.Message.CopyBothResponse;
import io.trane.ndbc.postgres.proto.Message.CopyData;
import io.trane.ndbc.postgres.proto.Message.CopyDone;
import io.trane.ndbc.postgres.proto.Message.CopyInResponse;
import io.trane.ndbc.postgres.proto.Message.CopyOutResponse;
import io.trane.ndbc.postgres.proto.Message.EmptyQueryResponse;
import io.trane.ndbc.postgres.proto.Message.FunctionCallResponse;
import io.trane.ndbc.postgres.proto.Message.InfoResponse;
import io.trane.ndbc.postgres.proto.Message.NoData;
import io.trane.ndbc.postgres.proto.Message.NotificationResponse;
import io.trane.ndbc.proto.BufferReader;

public class Decoder {

  private final AuthenticationRequestDecoder authenticationRequestDecoder;
  private final CommandCompleteDecoder commandCompleteDecoder;
  private final DataRowDecoder dataRowDecoder;
  private final InfoResponseFieldsDecoder infoResponseFieldsDecoder;
  private final RowDescriptionDecoder rowDescriptionDecoder;

  private final BindComplete bindComplete = new BindComplete();
  private final CloseComplete closeComplete = new CloseComplete();
  private final CopyDone copyDone = new CopyDone();
  private final EmptyQueryResponse emptyQueryResponse = new EmptyQueryResponse();
  private final NoData noData = new NoData();
  private final ParseComplete parseComplete = new ParseComplete();
  private final PortalSuspended portalSuspended = new PortalSuspended();

  public Decoder() {
    super();
    this.authenticationRequestDecoder = new AuthenticationRequestDecoder();
    this.commandCompleteDecoder = new CommandCompleteDecoder();
    this.dataRowDecoder = new DataRowDecoder();
    this.infoResponseFieldsDecoder = new InfoResponseFieldsDecoder();
    this.rowDescriptionDecoder = new RowDescriptionDecoder();
  }

  public Optional<Message> decode(BufferReader b) throws Exception {
    if (b.readableBytes() >= 5) {
      b.markReaderIndex();
      byte tpe = b.readByte();
      int length = b.readInt() - 4;
      if (b.readableBytes() >= length)
        return Optional.of(decode(tpe, b.readSlice(length)));
      else {
        b.resetReaderIndex();
        return Optional.empty();
      }
    } else
      return Optional.empty();
  }

  private final Message decode(byte tpe, BufferReader b) {
    switch (tpe) {
    case 'R':
      return authenticationRequestDecoder.decode(b);
    case 'K':
      return new BackendKeyData(b.readInt(), b.readInt());
    case '2':
      return bindComplete;
    case '3':
      return closeComplete;
    case 'C':
      return commandCompleteDecoder.decode(b);
    case 'd':
      return new CopyData(b.readBytes());
    case 'c':
      return copyDone;
    case 'G':
      return notImplemented(CopyInResponse.class);
    case 'H':
      return notImplemented(CopyOutResponse.class);
    case 'W':
      return notImplemented(CopyBothResponse.class);
    case 'D':
      return dataRowDecoder.decode(b);
    case 'I':
      return emptyQueryResponse;
    case 'E':
      return new InfoResponse.ErrorResponse(infoResponseFieldsDecoder.decode(b));
    case 'V':
      return notImplemented(FunctionCallResponse.class);
    case 'n':
      return noData;
    case 'N':
      return new InfoResponse.NoticeResponse(infoResponseFieldsDecoder.decode(b));
    case 'A':
      return new NotificationResponse(b.readInt(), b.readCString(), b.readCString());
    case 't':
      return new ParameterDescription(b.readInts(b.readShort()));
    case 'S':
      return new ParameterStatus(b.readCString(), b.readCString());
    case '1':
      return parseComplete;
    case 's':
      return portalSuspended;
    case 'Z':
      return new ReadyForQuery(b.readByte());
    case 'T':
      return rowDescriptionDecoder.decode(b);
    default:
      throw new IllegalStateException("Invalid server message type: " + (char) tpe);
    }
  }

  private Message notImplemented(Class<?> cls) {
    throw new UnsupportedOperationException("Decoder not implemented for class: " + cls);
  }
}
