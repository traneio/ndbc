package io.trane.ndbc.value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class LocalDateTimeValue extends Value<LocalDateTime> {

  public LocalDateTimeValue(final LocalDateTime value) {
    super(value);
  }

  @Override
  public final LocalDateTime getLocalDateTime() {
    return get();
  }

  @Override
  public final LocalDate getLocalDate() {
    return get().toLocalDate();
  }

  @Override
  public final LocalTime getLocalTime() {
    return get().toLocalTime();
  }
}
