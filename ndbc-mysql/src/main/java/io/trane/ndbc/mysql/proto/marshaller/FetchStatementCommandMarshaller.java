package io.trane.ndbc.mysql.proto.marshaller;

import io.trane.ndbc.mysql.proto.Message.FetchStatementCommand;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;

public class FetchStatementCommandMarshaller implements Marshaller<FetchStatementCommand> {

  @Override
  public void apply(final FetchStatementCommand command, final BufferWriter bw) {
    final PacketBufferWriter packet = new PacketBufferWriter(bw, 0);
    packet.writeByte((byte) 0x1C); // status [0x1C] COM_STMT_FETCH
    packet.writeUnsignedInt(command.statementId);
    packet.writeUnsignedInt(command.maxRows);
    packet.flush();
  }
}
