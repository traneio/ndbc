package io.trane.ndbc.value;

import java.time.LocalDate;

public class LocalDateValue extends Value<LocalDate> {

  public LocalDateValue(LocalDate value) {
    super(value);
  }

  @Override
  public LocalDate getLocalDate() {
    return get();
  }
}
