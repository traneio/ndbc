package io.trane.ndbc.value;

import java.time.LocalTime;

public final class LocalTimeArrayValue extends Value<LocalTime[]> {

	public LocalTimeArrayValue(final LocalTime[] value) {
		super(value);
	}

	@Override
	public final LocalTime[] getLocalTimeArray() {
		return get();
	}
}