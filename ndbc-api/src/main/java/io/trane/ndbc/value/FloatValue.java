package io.trane.ndbc.value;

import java.math.BigDecimal;

public final class FloatValue extends Value<Float> {

  public FloatValue(final Float value) {
    super(value);
  }

  @Override
  public final Float getFloat() {
    return get();
  }

  @Override
  public final BigDecimal getBigDecimal() {
    return new BigDecimal(get());
  }
}
