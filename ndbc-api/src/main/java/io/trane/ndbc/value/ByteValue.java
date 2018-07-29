package io.trane.ndbc.value;

import java.math.BigDecimal;

public final class ByteValue extends Value<Byte> {

	public ByteValue(final Byte value) {
		super(value);
	}

	@Override
	public final Byte getByte() {
		return get();
	}

	@Override
	public final Integer getInteger() {
		return new Integer(get());
	}

	@Override
	public final BigDecimal getBigDecimal() {
		return new BigDecimal(get());
	}

	@Override
	public final Double getDouble() {
		return new Double(get());
	}

	@Override
	public final Float getFloat() {
		return new Float(get());
	}

	@Override
	public final Long getLong() {
		return new Long(get());
	}

	@Override
	public final Boolean getBoolean() {
		return get() == (byte) 1;
	}
}
