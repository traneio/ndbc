package io.trane.ndbc.postgres.proto.marshaller;

import io.trane.ndbc.postgres.proto.Message.CancelRequest;
import io.trane.ndbc.proto.BufferWriter;

public final class CancelRequestMarshaller {

	public final void encode(final CancelRequest msg, final BufferWriter b) {
		b.writeInt(0);
		b.writeInt(80877102);
		b.writeInt(msg.processId);
		b.writeInt(msg.secretKey);
		b.writeLength(0);
	}
}
