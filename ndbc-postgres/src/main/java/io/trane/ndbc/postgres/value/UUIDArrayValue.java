package io.trane.ndbc.postgres.value;

import java.util.UUID;

public final class UUIDArrayValue extends PostgresValue<UUID[]> {

  public UUIDArrayValue(final UUID[] value) {
    super(value);
  }

  @Override
  public final UUID[] getUUIDArray() {
    return get();
  }
}