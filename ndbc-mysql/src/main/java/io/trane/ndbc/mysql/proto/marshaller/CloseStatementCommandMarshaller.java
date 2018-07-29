package io.trane.ndbc.mysql.proto.marshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.mysql.proto.Message.CloseStatementCommand;
import io.trane.ndbc.proto.BufferWriter;

public class CloseStatementCommandMarshaller {

	public void encode(final CloseStatementCommand command, final BufferWriter bw, final Charset charset) {
		PacketBufferWriter packet = new PacketBufferWriter(bw, 0, charset);
		packet.writeByte(command.command);
		packet.writeUnsignedInt(command.statementId);
		packet.flush();
	}
}
