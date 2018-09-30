package io.trane.ndbc.postgres.value;

import java.time.LocalDateTime;

import io.trane.ndbc.value.Value;

public final class LocalDateTimeArrayValue extends Value<LocalDateTime[]> {

  public LocalDateTimeArrayValue(final LocalDateTime[] value) {
    super(value);
  }

  @Override
  public final LocalDateTime[] getLocalDateTimeArray() {
    return get();
  }
}