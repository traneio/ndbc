package io.trane.ndbc.mysql.proto.unmarshaller;

import io.trane.ndbc.mysql.proto.Message.PrepareOk;
import io.trane.ndbc.mysql.proto.PacketBufferReader;

public class PrepareOkUnmarshaller extends MysqlUnmarshaller<PrepareOk> {

  private final static int OK_BYTE = 0x00;

  @Override
  protected boolean acceptsHeader(final int header) {
    return header == OK_BYTE;
  }

  @Override
  public PrepareOk decode(final int header, final PacketBufferReader p) {
    assert ((p.readByte() & 0xFF) == OK_BYTE);

    final long statementId = p.readUnsignedInt();
    final int numOfColumns = p.readUnsignedShort();
    final int numOfParameters = p.readUnsignedShort();
    p.readByte(); // reserved_1
    final int warningCount = p.readUnsignedShort();

    return new PrepareOk(statementId, numOfColumns, numOfParameters, warningCount);
  }
}
