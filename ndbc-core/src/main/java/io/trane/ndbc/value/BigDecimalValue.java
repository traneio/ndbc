package io.trane.ndbc.value;

import java.math.BigDecimal;

public final class BigDecimalValue extends Value<BigDecimal> {

  public BigDecimalValue(final BigDecimal value) {
    super(value);
  }

  @Override
  public final BigDecimal getBigDecimal() {
    return get();
  }
}
