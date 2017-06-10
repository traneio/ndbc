package io.trane.ndbc.value;

import java.math.BigDecimal;

public final class IntegerValue extends Value<Integer> {
  public IntegerValue(final Integer value) {
    super(value);
  }

  @Override
  public final Integer getInteger() {
    return get();
  }
  
  @Override
  public final BigDecimal getBigDecimal() {
    return new BigDecimal(get());
  }
  
  @Override
  public final Double getDouble() {
    return new Double(get());
  }
  
  @Override
  public final Float getFloat() {
    return new Float(get());
  }
  
  @Override
  public final Long getLong() {
    return new Long(get());
  }
  
  @Override
  public final Boolean getBoolean() {
    return get() == 1;
  }
}