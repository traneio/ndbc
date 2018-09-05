package io.trane.ndbc.value;

import java.time.LocalDate;

public final class LocalDateArrayValue extends Value<LocalDate[]> {

  public LocalDateArrayValue(final LocalDate[] value) {
    super(value);
  }

  @Override
  public final LocalDate[] getLocalDateArray() {
    return get();
  }
}