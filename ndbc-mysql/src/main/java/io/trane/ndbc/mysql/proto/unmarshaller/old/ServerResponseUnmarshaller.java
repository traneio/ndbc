package io.trane.ndbc.mysql.proto.unmarshaller.old;

import io.trane.ndbc.mysql.proto.Message.EofResponse;
import io.trane.ndbc.mysql.proto.Message.ErrPacketMessage;
import io.trane.ndbc.mysql.proto.Message.OkPacket;
import io.trane.ndbc.mysql.proto.Message.Terminator;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.Unmarshaller;

public class ServerResponseUnmarshaller implements Unmarshaller<Terminator> {
  public final static int OK_BYTE    = 0x00;
  public final static int ERROR_BYTE = 0xFF;
  public final static int EOF_BYTE   = 0xFE;

  @Override
  public final Terminator apply(final BufferReader br) {
    final PacketBufferReader packet = new PacketBufferReader(br);
    final int responseType = packet.readByte() & 0xFF;
    switch (responseType) {
      case (OK_BYTE):
        return decodeOk(packet);
      case (ERROR_BYTE):
        return decodeError(packet);
      case (EOF_BYTE):
        return decodeEof(packet);
      default:
        throw new UnsupportedOperationException("Unsupported response from server " + responseType);
    }
  }

  public static EofResponse decodeEof(final PacketBufferReader packet) {
    packet.readByte();
    return new EofResponse(packet.readUnsignedShort(), packet.readUnsignedShort());
  }

  public static OkPacket decodeOk(final PacketBufferReader packet) {
    final long affectedRows = packet.readVariableLong();
    final long insertedId = packet.readVariableLong();
    final int serverStatus = packet.readUnsignedShort();
    final int warningCount = packet.readUnsignedShort();
    final String message = new String(packet.readBytes());
    return new OkPacket(affectedRows, insertedId, serverStatus, warningCount, message);
  }

  public static ErrPacketMessage decodeError(final PacketBufferReader packet) {
    final byte[] bytes = packet.readBytes();
    final String errorMessage = new String(bytes);
    return new ErrPacketMessage(errorMessage);
  }

}
