package io.trane.ndbc.postgres.value;

import java.time.LocalDateTime;

public final class LocalDateTimeArrayValue extends PostgresValue<LocalDateTime[]> {

  public LocalDateTimeArrayValue(final LocalDateTime[] value) {
    super(value);
  }

  @Override
  public final LocalDateTime[] getLocalDateTimeArray() {
    return get();
  }
}