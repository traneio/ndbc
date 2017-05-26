package io.trane.ndbc.value;

public class ShortValue extends Value<Short> {

  public ShortValue(Short value) {
    super(value);
  }

  @Override
  public Short getShort() {
    return get();
  }
}
