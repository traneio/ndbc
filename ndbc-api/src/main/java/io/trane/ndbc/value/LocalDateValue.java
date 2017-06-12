package io.trane.ndbc.value;

import java.time.LocalDate;

public final class LocalDateValue extends Value<LocalDate> {

  public LocalDateValue(final LocalDate value) {
    super(value);
  }

  @Override
  public final LocalDate getLocalDate() {
    return get();
  }
}
