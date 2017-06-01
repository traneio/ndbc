package io.trane.ndbc.value;

import java.time.LocalDateTime;

public final class LocalDateTimeValue extends Value<LocalDateTime> {

  public LocalDateTimeValue(final LocalDateTime value) {
    super(value);
  }

  @Override
  public final LocalDateTime getLocalDateTime() {
    return get();
  }
}
