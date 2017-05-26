package io.trane.ndbc.value;

import java.math.BigDecimal;

public class BigDecimalValue extends Value<BigDecimal> {

  public BigDecimalValue(BigDecimal value) {
    super(value);
  }

  @Override
  public BigDecimal getBigDecimal() {
    return get();
  }
}
