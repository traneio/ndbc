package io.trane.ndbc.postgres.proto.marshaller;

import io.trane.ndbc.postgres.proto.Message.Describe;
import io.trane.ndbc.proto.BufferWriter;

public final class DescribeMarshaller {

	public final void encode(final Describe msg, final BufferWriter b) {
		b.writeChar('D');
		b.writeInt(0);

		if (msg instanceof Describe.DescribePreparedStatement)
			b.writeChar('S');
		else if (msg instanceof Describe.DescribePortal)
			b.writeChar('P');
		else
			throw new IllegalStateException("Invalid describe message: " + msg);

		b.writeCString(msg.name);
		b.writeLength(1);
	}
}
