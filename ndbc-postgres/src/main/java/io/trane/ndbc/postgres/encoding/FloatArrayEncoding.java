package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.value.FloatArrayValue;

final class FloatArrayEncoding extends ArrayEncoding<Float, FloatArrayValue> {

	private final FloatEncoding floatEncoding;
	private final Float[] emptyArray = new Float[0];

	public FloatArrayEncoding(FloatEncoding floatEncoding) {
		this.floatEncoding = floatEncoding;
	}

	@Override
	public final Integer oid() {
		return Oid.FLOAT4_ARRAY;
	}

	@Override
	public final Class<FloatArrayValue> valueClass() {
		return FloatArrayValue.class;
	}

	@Override
	protected Float[] newArray(int length) {
		return new Float[length];
	}

	@Override
	protected Float[] emptyArray() {
		return emptyArray;
	}

	@Override
	protected Encoding<Float, ?> itemEncoding() {
		return floatEncoding;
	}

	@Override
	protected FloatArrayValue box(Float[] value) {
		return new FloatArrayValue(value);
	}

	@Override
	protected Float[] unbox(FloatArrayValue value) {
		return value.getFloatArray();
	}
}
