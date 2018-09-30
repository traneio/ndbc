package io.trane.ndbc.postgres.value;

import java.util.UUID;

import io.trane.ndbc.value.Value;

public final class UUIDArrayValue extends Value<UUID[]> {

  public UUIDArrayValue(final UUID[] value) {
    super(value);
  }

  @Override
  public final UUID[] getUUIDArray() {
    return get();
  }
}