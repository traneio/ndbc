package io.trane.ndbc.postgres.value;

import io.trane.ndbc.value.Value;

public final class DoubleArrayValue extends Value<Double[]> {

  public DoubleArrayValue(final Double[] value) {
    super(value);
  }

  @Override
  public final Double[] getDoubleArray() {
    return get();
  }
}