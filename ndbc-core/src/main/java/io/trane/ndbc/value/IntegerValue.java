package io.trane.ndbc.value;

public final class IntegerValue extends Value<Integer> {
  public IntegerValue(final Integer value) {
    super(value);
  }

  @Override
  public final Integer getInteger() {
    return get();
  }
}