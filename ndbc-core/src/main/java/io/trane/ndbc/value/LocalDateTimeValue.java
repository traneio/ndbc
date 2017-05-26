package io.trane.ndbc.value;

import java.time.LocalDateTime;

public class LocalDateTimeValue extends Value<LocalDateTime> {

  public LocalDateTimeValue(LocalDateTime value) {
    super(value);
  }

  @Override
  public LocalDateTime getLocalDateTime() {
    return get();
  }
}
