package io.trane.ndbc.value;

public final class ShortValue extends Value<Short> {

  public ShortValue(final Short value) {
    super(value);
  }

  @Override
  public final Short getShort() {
    return get();
  }
}
