package io.trane.ndbc.value;

public final class BooleanValue extends Value<Boolean> {

  public BooleanValue(final Boolean value) {
    super(value);
  }

  @Override
  public final Boolean getBoolean() {
    return get();
  }
}
