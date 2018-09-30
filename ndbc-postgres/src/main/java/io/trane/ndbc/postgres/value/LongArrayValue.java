package io.trane.ndbc.postgres.value;

import io.trane.ndbc.value.Value;

public final class LongArrayValue extends Value<Long[]> {

  public LongArrayValue(final Long[] value) {
    super(value);
  }

  @Override
  public final Long[] getLongArray() {
    return get();
  }
}