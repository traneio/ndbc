package io.trane.ndbc.value;

public final class FloatValue extends Value<Float> {

  public FloatValue(final Float value) {
    super(value);
  }

  @Override
  public final Float getFloat() {
    return get();
  }
}
