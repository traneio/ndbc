package io.trane.ndbc.value;

import java.math.BigDecimal;

public final class DoubleValue extends Value<Double> {

	public DoubleValue(final Double value) {
		super(value);
	}

	@Override
	public final Double getDouble() {
		return get();
	}

	@Override
	public final Float getFloat() {
		return get().floatValue();
	}

	@Override
	public final BigDecimal getBigDecimal() {
		return new BigDecimal(get());
	}
}
