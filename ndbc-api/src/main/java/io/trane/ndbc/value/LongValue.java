package io.trane.ndbc.value;

import java.math.BigDecimal;

public final class LongValue extends Value<Long> {

  public LongValue(final Long value) {
    super(value);
  }

  @Override
  public final Long getLong() {
    return get();
  }
  
  @Override
  public final BigDecimal getBigDecimal() {
    return new BigDecimal(get());
  }
}
