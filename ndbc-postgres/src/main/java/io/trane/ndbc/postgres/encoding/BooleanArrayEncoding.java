package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.value.BooleanArrayValue;

final class BooleanArrayEncoding extends ArrayEncoding<Boolean, BooleanArrayValue> {

	private final BooleanEncoding booleanEncoding;
	private final Boolean[] emptyArray = new Boolean[0];

	public BooleanArrayEncoding(final BooleanEncoding booleanEncoding, Charset charset) {
		super(charset);
		this.booleanEncoding = booleanEncoding;
	}

	@Override
	public final Integer oid() {
		return Oid.BOOL_ARRAY;
	}

	@Override
	public final Class<BooleanArrayValue> valueClass() {
		return BooleanArrayValue.class;
	}

	@Override
	protected Boolean[] newArray(final int length) {
		return new Boolean[length];
	}

	@Override
	protected Boolean[] emptyArray() {
		return emptyArray;
	}

	@Override
	protected Encoding<Boolean, ?> itemEncoding() {
		return booleanEncoding;
	}

	@Override
	protected BooleanArrayValue box(final Boolean[] value) {
		return new BooleanArrayValue(value);
	}
}
