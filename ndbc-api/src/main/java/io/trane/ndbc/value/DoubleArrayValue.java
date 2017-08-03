package io.trane.ndbc.value;

public final class DoubleArrayValue extends Value<Double[]> {
  
  public DoubleArrayValue(final Double[] value) {
    super(value);
  }

  @Override
  public final Double[] getDoubleArray() {
    return get();
  }
}