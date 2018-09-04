package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.postgres.proto.Message.DataRow;
import io.trane.ndbc.proto.BufferReader;;

public final class DataRowUnmarshaller extends PostgresUnmarshaller<DataRow> {

	public DataRowUnmarshaller(Charset charset) {
		super(charset);
	}

	@Override
	protected boolean acceptsType(byte tpe) {
		return tpe == 'D';
	}

	@Override
	public final DataRow decode(final byte tpe, final BufferReader b) {
		final short columns = b.readShort();
		final BufferReader[] values = new BufferReader[columns];
		for (short i = 0; i < columns; i++) {
			final int length = b.readInt();
			if (length == -1)
				values[i] = null;
			else {
				final BufferReader slice = b.readSlice(length);
				slice.retain();
				values[i] = slice;
			}
		}
		return new DataRow(values);
	}
}
