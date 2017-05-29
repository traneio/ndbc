package io.trane.ndbc.value;

public class DoubleValue extends Value<Double> {

  public DoubleValue(Double value) {
    super(value);
  }

  @Override
  public Double getDouble() {
    return get();
  }
}
