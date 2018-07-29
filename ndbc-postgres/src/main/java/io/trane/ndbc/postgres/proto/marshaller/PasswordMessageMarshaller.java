package io.trane.ndbc.postgres.proto.marshaller;

import io.trane.ndbc.postgres.proto.Message.PasswordMessage;
import io.trane.ndbc.proto.BufferWriter;

public final class PasswordMessageMarshaller {

	public final void encode(final PasswordMessage msg, final BufferWriter b) {
		b.writeChar('p');
		b.writeInt(0);
		b.writeCString(msg.password);
		b.writeLength(1);
	}
}
