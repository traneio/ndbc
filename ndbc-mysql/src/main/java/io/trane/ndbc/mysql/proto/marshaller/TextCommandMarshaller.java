package io.trane.ndbc.mysql.proto.marshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.Message.TextCommand;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.proto.BufferWriter;

public class TextCommandMarshaller {

	public void encode(final TextCommand command, final BufferWriter bw, final Charset charset) {
		final PacketBufferWriter packet = new PacketBufferWriter(bw, 0, charset);
		packet.writeByte(command.getCommand());
		packet.writeString(command.getSqlStatement());
		packet.flush();
	}
}
