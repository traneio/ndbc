package io.trane.ndbc.value;

import java.util.UUID;

public final class UUIDArrayValue extends Value<UUID[]> {

	public UUIDArrayValue(final UUID[] value) {
		super(value);
	}

	@Override
	public final UUID[] getUUIDArray() {
		return get();
	}
}