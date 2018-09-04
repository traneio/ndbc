package io.trane.ndbc.postgres.proto.marshaller;

import io.trane.ndbc.postgres.encoding.EncodingRegistry;
import io.trane.ndbc.postgres.proto.Message.Parse;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;
import io.trane.ndbc.value.Value;

public final class ParseMarshaller implements Marshaller<Parse> {

	private final EncodingRegistry encoding;

	public ParseMarshaller(final EncodingRegistry encoding) {
		this.encoding = encoding;
	}

	public final void apply(final Parse msg, final BufferWriter b) {
		b.writeChar('P');
		b.writeInt(0);

		b.writeCString(msg.destinationName);
		b.writeCString(msg.query);
		b.writeShort((short) msg.params.size());

		for (final Value<?> v : msg.params)
			b.writeInt(encoding.oid(v));

		b.writeLength(1);
	}
}
