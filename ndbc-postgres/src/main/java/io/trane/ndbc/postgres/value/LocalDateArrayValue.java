package io.trane.ndbc.postgres.value;

import java.time.LocalDate;

public final class LocalDateArrayValue extends PostgresValue<LocalDate[]> {

  public LocalDateArrayValue(final LocalDate[] value) {
    super(value);
  }

  @Override
  public final LocalDate[] getLocalDateArray() {
    return get();
  }
}