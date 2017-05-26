package io.trane.ndbc.value;

import java.math.BigDecimal;

public class FloatValue extends Value<Float> {

  public FloatValue(Float value) {
    super(value);
  }

  @Override
  public Float getFloat() {
    return get();
  }
}
