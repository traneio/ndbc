package io.trane.ndbc.postgres.value;

import io.trane.ndbc.value.Value;

public final class FloatArrayValue extends Value<Float[]> {

  public FloatArrayValue(final Float[] value) {
    super(value);
  }

  @Override
  public final Float[] getFloatArray() {
    return get();
  }
}