package io.trane.ndbc.value;

public class FloatValue extends Value<Float> {

  public FloatValue(Float value) {
    super(value);
  }

  @Override
  public Float getFloat() {
    return get();
  }
}
