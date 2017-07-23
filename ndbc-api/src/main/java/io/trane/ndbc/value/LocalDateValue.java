package io.trane.ndbc.value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class LocalDateValue extends Value<LocalDate> {

  public LocalDateValue(final LocalDate value) {
    super(value);
  }

  @Override
  public final LocalDate getLocalDate() {
    return get();
  }
  
  @Override
  public LocalDateTime getLocalDateTime() {
    return LocalDateTime.of(get(), LocalTime.MIDNIGHT);
  }
}
