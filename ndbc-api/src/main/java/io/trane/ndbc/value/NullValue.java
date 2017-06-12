package io.trane.ndbc.value;

public final class NullValue extends Value<Object> {
  public NullValue() {
    super(null);
  }

  @Override
  public final boolean isNull() {
    return true;
  }
}
