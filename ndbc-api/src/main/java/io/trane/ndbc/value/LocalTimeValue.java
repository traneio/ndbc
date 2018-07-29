package io.trane.ndbc.value;

import java.time.LocalTime;

public final class LocalTimeValue extends Value<LocalTime> {

	public LocalTimeValue(final LocalTime value) {
		super(value);
	}

	@Override
	public final LocalTime getLocalTime() {
		return get();
	}
}
