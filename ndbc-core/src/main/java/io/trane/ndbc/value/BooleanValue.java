package io.trane.ndbc.value;

public class BooleanValue extends Value<Boolean> {

  public BooleanValue(Boolean value) {
    super(value);
  }

  @Override
  public Boolean getBoolean() {
    return get();
  }
}
