package io.trane.ndbc.value;

public final class LongValue extends Value<Long> {

  public LongValue(final Long value) {
    super(value);
  }

  @Override
  public final Long getLong() {
    return get();
  }
}
