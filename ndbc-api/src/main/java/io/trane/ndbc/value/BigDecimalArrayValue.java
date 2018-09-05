package io.trane.ndbc.value;

import java.math.BigDecimal;

public final class BigDecimalArrayValue extends Value<BigDecimal[]> {

  public BigDecimalArrayValue(final BigDecimal[] value) {
    super(value);
  }

  @Override
  public final BigDecimal[] getBigDecimalArray() {
    return get();
  }
}