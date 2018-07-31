package io.trane.ndbc.mysql.proto.marshaller;

import java.nio.charset.Charset;
import java.util.logging.Logger;

import io.trane.ndbc.mysql.proto.Message.CloseStatementCommand;
import io.trane.ndbc.mysql.proto.Message.ExecuteStatementCommand;
import io.trane.ndbc.mysql.proto.Message.HandshakeResponseMessage;
import io.trane.ndbc.mysql.proto.Message.TextCommand;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.ClientMessage;
import io.trane.ndbc.proto.Marshaller;

public class MysqlMarshaller implements Marshaller {
	private static final Logger log = Logger.getLogger(MysqlMarshaller.class.getName());
	private final HandshakeResponsePacketMarshaller handshakeResponsePacketMarshaller = new HandshakeResponsePacketMarshaller();
	private final TextCommandMarshaller textCommandMarshaller = new TextCommandMarshaller();
	private final ExecuteStatementCommandMarshaller executeStatementCommandMarshaller = new ExecuteStatementCommandMarshaller();
	private final CloseStatementCommandMarshaller closeStatementCommandMarshaller = new CloseStatementCommandMarshaller();
	private final Charset charset = Charset.forName("UTF-8"); // TODO: Move to config

	@Override
	public void encode(final ClientMessage msg, final BufferWriter bw) {
		if (msg instanceof HandshakeResponseMessage) {
			handshakeResponsePacketMarshaller.encode((HandshakeResponseMessage) msg, bw, charset);
		} else if (msg instanceof TextCommand) {
			textCommandMarshaller.encode((TextCommand) msg, bw, charset);
		} else if (msg instanceof ExecuteStatementCommand) {
			executeStatementCommandMarshaller.encode((ExecuteStatementCommand) msg, bw, charset);
		} else if (msg instanceof CloseStatementCommand) {
			closeStatementCommandMarshaller.encode((CloseStatementCommand) msg, bw, charset);
		} else {
			log.severe("Invalid client message: " + msg);
		}
	}
}
