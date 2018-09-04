package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.BooleanValue;

final class BooleanEncoding extends Encoding<Boolean, BooleanValue> {

	public BooleanEncoding(Charset charset) {
		super(charset);
	}

	@Override
	public final Integer oid() {
		return Oid.BOOL;
	}

	@Override
	public final Class<BooleanValue> valueClass() {
		return BooleanValue.class;
	}

	@Override
	public final String encodeText(final Boolean value) {
		return value ? "T" : "F";
	}

	@Override
	public final Boolean decodeText(final String value) {
		final String upperCase = value.toUpperCase();
		return "T".equals(upperCase) || "TRUE".equals(upperCase);
	}

	@Override
	public final void encodeBinary(final Boolean value, final BufferWriter b) {
		b.writeByte((byte) (value ? 1 : 0));
	}

	@Override
	public final Boolean decodeBinary(final BufferReader b) {
		return b.readByte() != 0;
	}

	@Override
	protected BooleanValue box(final Boolean value) {
		return new BooleanValue(value);
	}
}
