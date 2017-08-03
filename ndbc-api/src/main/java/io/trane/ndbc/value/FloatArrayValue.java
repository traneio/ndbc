package io.trane.ndbc.value;

public final class FloatArrayValue extends Value<Float[]> {
  
  public FloatArrayValue(final Float[] value) {
    super(value);
  }

  @Override
  public final Float[] getFloatArray() {
    return get();
  }
}