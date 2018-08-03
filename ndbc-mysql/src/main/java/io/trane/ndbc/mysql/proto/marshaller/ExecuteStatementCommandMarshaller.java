package io.trane.ndbc.mysql.proto.marshaller;

import io.trane.ndbc.mysql.proto.Message.ExecuteStatementCommand;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;

public class ExecuteStatementCommandMarshaller implements Marshaller<ExecuteStatementCommand> {

  public void apply(final ExecuteStatementCommand command, final BufferWriter bw) {
    final PacketBufferWriter packet = new PacketBufferWriter(bw, 0);
    packet.writeByte(command.command);
    packet.writeUnsignedInt(command.statementId);
    packet.writeByte(command.flags);
    packet.writeInt(command.iterationCount);
    packet.flush();
  }
}
