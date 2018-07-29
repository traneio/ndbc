package io.trane.ndbc.postgres.proto.unmarshaller;

import io.trane.ndbc.postgres.proto.Message.RowDescription;
import io.trane.ndbc.proto.BufferReader;

final class RowDescriptionUnmarshaller {

	public final RowDescription decode(final BufferReader b) {
		final short size = b.readShort();
		final RowDescription.Field[] fields = new RowDescription.Field[size];
		for (int i = 0; i < size; i++)
			fields[i] = new RowDescription.Field(b.readCString(), b.readInt(), b.readShort(), b.readInt(),
					b.readShort(), b.readInt(), b.readShort());
		return new RowDescription(fields);
	}
}
