package io.trane.ndbc.mysql.proto.unmarshaller;

import io.trane.ndbc.mysql.proto.Message.ErrorResponseMessage;
import io.trane.ndbc.mysql.proto.Message.OkPrepareStatement;
import io.trane.ndbc.mysql.proto.Message.ServerResponseMessage;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.proto.BufferReader;

public class PrepareStatementOkUnmarshaller {
  public final static int OK_BYTE = 0x00;
  public final static int ERROR_BYTE = 0xFF;

  public final ServerResponseMessage decode(final BufferReader br) {
    PacketBufferReader packet = new PacketBufferReader(br);
    int responseType = packet.readByte() & 0xFF;
    switch (responseType) {
    case (OK_BYTE):
      return decodeOk(packet);
    case (ERROR_BYTE):
      return decodeError(packet);
    default:
      throw new UnsupportedOperationException("Unsupported response from server - buggy");
    }
  }

  private OkPrepareStatement decodeOk(final PacketBufferReader packet) {
    long statementId = packet.readUnsignedInt();
    int numOfColumns = packet.readUnsignedShort();
    int numOfParameters = packet.readUnsignedShort();
    packet.readByte();
    int warningCount = packet.readUnsignedShort();

    return new OkPrepareStatement(statementId, numOfColumns, numOfParameters, warningCount);
  }

  public static ErrorResponseMessage decodeError(final PacketBufferReader packet) {
    byte[] bytes = packet.readBytes();
    String errorMessage = new String(bytes);
    return new ErrorResponseMessage(errorMessage);
  }
}