package io.trane.ndbc.mysql.proto.marshaller;

import static io.trane.ndbc.mysql.proto.Message.*;


import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.proto.BufferWriter;

import java.nio.charset.Charset;

public class TextCommandMarshaller {

  public void encode(TextCommand command, BufferWriter bw, Charset charset) {
    PacketBufferWriter packet = new PacketBufferWriter(bw, 0, charset);
    packet.writeByte(command.getCommand());
    packet.writeString(command.getSqlStatement());
    packet.flush();
  }
}
