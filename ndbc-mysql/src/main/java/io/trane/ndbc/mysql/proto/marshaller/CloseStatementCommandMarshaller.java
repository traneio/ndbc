package io.trane.ndbc.mysql.proto.marshaller;

import io.trane.ndbc.mysql.proto.Message.CloseStatementCommand;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;

public class CloseStatementCommandMarshaller implements Marshaller<CloseStatementCommand> {

  @Override
  public void apply(final CloseStatementCommand command, final BufferWriter bw) {
    final PacketBufferWriter packet = new PacketBufferWriter(bw, 0);
    packet.writeByte(command.command);
    packet.writeUnsignedInt(command.statementId);
    packet.flush();
  }
}
