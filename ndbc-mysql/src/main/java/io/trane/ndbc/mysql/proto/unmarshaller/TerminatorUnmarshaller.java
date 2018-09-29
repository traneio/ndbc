package io.trane.ndbc.mysql.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.Message.EofPacket;
import io.trane.ndbc.mysql.proto.Message.ErrPacketMessage;
import io.trane.ndbc.mysql.proto.Message.OkPacket;
import io.trane.ndbc.mysql.proto.Message.Terminator;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.ServerStatus;

public class TerminatorUnmarshaller extends MysqlUnmarshaller<Terminator> {

  public final static int OK_BYTE    = 0x00;
  public final static int ERROR_BYTE = 0xFF;
  public final static int EOF_BYTE   = 0xFE;

  public static final boolean isTerminator(final int header, final int readableBytes) {
    return readableBytes > 0 && (header == OK_BYTE) || (header == ERROR_BYTE) || (header == EOF_BYTE);
  }

  private final Charset charset;

  public TerminatorUnmarshaller(final Charset charset) {
    this.charset = charset;
  }

  @Override
  protected boolean acceptsHeader(final int header, final int readableBytes) {
    return isTerminator(header, readableBytes);
  }

  @Override
  public Terminator decode(final PacketBufferReader p) {
    switch (p.readByte() & 0xFF) {
      case OK_BYTE:
        final long affectedRows = p.readVariableLong();
        final long insertedId = p.readVariableLong();
        final int serverStatus = p.readUnsignedShort();
        final int warningCount = p.readUnsignedShort();
        final String message = new String(p.readBytes(), charset);
        return new OkPacket(affectedRows, insertedId, new ServerStatus(serverStatus), warningCount, message);
      case ERROR_BYTE:
        return new ErrPacketMessage(p.readString(charset));
      case EOF_BYTE:
        return new EofPacket(p.readUnsignedShort(), new ServerStatus(p.readUnsignedShort()));
      default:
        throw new IllegalStateException("Can't decode terminator message");
    }
  }
}
