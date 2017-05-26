package io.trane.ndbc.value;

public class LongValue extends Value<Long> {

  public LongValue(Long value) {
    super(value);
  }

  @Override
  public Long getLong() {
    return get();
  }
}
