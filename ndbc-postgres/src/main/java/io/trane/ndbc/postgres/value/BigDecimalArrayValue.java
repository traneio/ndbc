package io.trane.ndbc.postgres.value;

import java.math.BigDecimal;

public final class BigDecimalArrayValue extends PostgresValue<BigDecimal[]> {

  public BigDecimalArrayValue(final BigDecimal[] value) {
    super(value);
  }

  @Override
  public final BigDecimal[] getBigDecimalArray() {
    return get();
  }
}