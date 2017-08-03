package io.trane.ndbc.value;

public final class BooleanArrayValue extends Value<Boolean[]> {

  public BooleanArrayValue(final Boolean[] value) {
    super(value);
  }

  @Override
  public final Boolean[] getBooleanArray() {
    return get();
  }
}
