package io.trane.ndbc.postgres.value;

import java.math.BigDecimal;

import io.trane.ndbc.value.Value;

public final class BigDecimalArrayValue extends Value<BigDecimal[]> {

  public BigDecimalArrayValue(final BigDecimal[] value) {
    super(value);
  }

  @Override
  public final BigDecimal[] getBigDecimalArray() {
    return get();
  }
}