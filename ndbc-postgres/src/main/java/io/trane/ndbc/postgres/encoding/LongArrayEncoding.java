package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.value.LongArrayValue;

final class LongArrayEncoding extends ArrayEncoding<Long, LongArrayValue> {

	private final LongEncoding longEncoding;
	private final Long[] emptyArray = new Long[0];

	public LongArrayEncoding(final LongEncoding longEncoding) {
		this.longEncoding = longEncoding;
	}

	@Override
	public final Integer oid() {
		return Oid.INT8_ARRAY;
	}

	@Override
	public final Class<LongArrayValue> valueClass() {
		return LongArrayValue.class;
	}

	@Override
	protected Long[] newArray(final int length) {
		return new Long[length];
	}

	@Override
	protected Long[] emptyArray() {
		return emptyArray;
	}

	@Override
	protected Encoding<Long, ?> itemEncoding() {
		return longEncoding;
	}

	@Override
	protected LongArrayValue box(final Long[] value) {
		return new LongArrayValue(value);
	}

	@Override
	protected Long[] unbox(final LongArrayValue value) {
		return value.getLongArray();
	}
}
