package io.trane.ndbc.value;

public class NullValue extends Value<Object> {
  public NullValue() {
    super(null);
  }

  @Override
  public boolean isNull() {
    return true;
  }
}
