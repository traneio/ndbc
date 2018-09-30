package io.trane.ndbc.postgres.value;

import java.time.LocalDate;

import io.trane.ndbc.value.Value;

public final class LocalDateArrayValue extends Value<LocalDate[]> {

  public LocalDateArrayValue(final LocalDate[] value) {
    super(value);
  }

  @Override
  public final LocalDate[] getLocalDateArray() {
    return get();
  }
}