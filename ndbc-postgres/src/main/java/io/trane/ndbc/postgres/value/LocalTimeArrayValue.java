package io.trane.ndbc.postgres.value;

import java.time.LocalTime;

public final class LocalTimeArrayValue extends PostgresValue<LocalTime[]> {

  public LocalTimeArrayValue(final LocalTime[] value) {
    super(value);
  }

  @Override
  public final LocalTime[] getLocalTimeArray() {
    return get();
  }
}