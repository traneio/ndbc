package io.trane.ndbc.mysql.proto.marshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.Message.ExecuteStatementCommand;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.proto.BufferWriter;

public class ExecuteStatementCommandMarshaller {

  public void encode(final ExecuteStatementCommand command, final BufferWriter bw, final Charset charset) {
    PacketBufferWriter packet = new PacketBufferWriter(bw, 0, charset);
    packet.writeByte(command.command);
    packet.writeUnsignedInt(command.statementId);
    packet.writeByte(command.flags);
    packet.writeInt(command.iterationCount);
    packet.flush();
  }
}
