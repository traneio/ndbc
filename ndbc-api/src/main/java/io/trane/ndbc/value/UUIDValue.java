package io.trane.ndbc.value;

import java.util.UUID;

public final class UUIDValue extends Value<UUID> {

	public UUIDValue(final UUID value) {
		super(value);
	}

	@Override
	public final UUID getUUID() {
		return get();
	}

	@Override
	public String getString() {
		return get().toString();
	}
}
