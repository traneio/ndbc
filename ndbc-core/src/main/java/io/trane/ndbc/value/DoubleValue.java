package io.trane.ndbc.value;

public final class DoubleValue extends Value<Double> {

  public DoubleValue(final Double value) {
    super(value);
  }

  @Override
  public final Double getDouble() {
    return get();
  }
}
