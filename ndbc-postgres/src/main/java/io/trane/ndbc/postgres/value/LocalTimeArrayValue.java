package io.trane.ndbc.postgres.value;

import java.time.LocalTime;

import io.trane.ndbc.value.Value;

public final class LocalTimeArrayValue extends Value<LocalTime[]> {

  public LocalTimeArrayValue(final LocalTime[] value) {
    super(value);
  }

  @Override
  public final LocalTime[] getLocalTimeArray() {
    return get();
  }
}