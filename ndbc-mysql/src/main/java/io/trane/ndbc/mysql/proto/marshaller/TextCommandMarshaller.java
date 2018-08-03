package io.trane.ndbc.mysql.proto.marshaller;

import io.trane.ndbc.mysql.proto.Message.TextCommand;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;

public class TextCommandMarshaller implements Marshaller<TextCommand> {

  @Override
  public void apply(final TextCommand command, final BufferWriter bw) {
    final PacketBufferWriter packet = new PacketBufferWriter(bw, 0);
    packet.writeByte(command.getCommand());
    packet.writeString(command.getSqlStatement());
    packet.flush();
  }
}
