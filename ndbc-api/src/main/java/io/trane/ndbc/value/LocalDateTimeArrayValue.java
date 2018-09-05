package io.trane.ndbc.value;

import java.time.LocalDateTime;

public final class LocalDateTimeArrayValue extends Value<LocalDateTime[]> {

  public LocalDateTimeArrayValue(final LocalDateTime[] value) {
    super(value);
  }

  @Override
  public final LocalDateTime[] getLocalDateTimeArray() {
    return get();
  }
}