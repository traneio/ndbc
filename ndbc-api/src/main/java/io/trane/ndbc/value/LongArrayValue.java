package io.trane.ndbc.value;

public final class LongArrayValue extends Value<Long[]> {
  
  public LongArrayValue(final Long[] value) {
    super(value);
  }

  @Override
  public final Long[] getLongArray() {
    return get();
  }
}