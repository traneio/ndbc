package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.value.IntegerArrayValue;

final class IntegerArrayEncoding extends ArrayEncoding<Integer, IntegerArrayValue> {

	private final IntegerEncoding integerEncoding;
	private final Integer[] emptyArray = new Integer[0];

	public IntegerArrayEncoding(final IntegerEncoding integerEncoding) {
		this.integerEncoding = integerEncoding;
	}

	@Override
	public final Integer oid() {
		return Oid.INT4_ARRAY;
	}

	@Override
	public final Class<IntegerArrayValue> valueClass() {
		return IntegerArrayValue.class;
	}

	@Override
	protected Integer[] newArray(final int length) {
		return new Integer[length];
	}

	@Override
	protected Integer[] emptyArray() {
		return emptyArray;
	}

	@Override
	protected Encoding<Integer, ?> itemEncoding() {
		return integerEncoding;
	}

	@Override
	protected IntegerArrayValue box(final Integer[] value) {
		return new IntegerArrayValue(value);
	}

	@Override
	protected Integer[] unbox(final IntegerArrayValue value) {
		return value.getIntegerArray();
	}
}
