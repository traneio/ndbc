package io.trane.ndbc.postgres.value;

import io.trane.ndbc.value.Value;

public final class IntegerArrayValue extends Value<Integer[]> {

  public IntegerArrayValue(final Integer[] value) {
    super(value);
  }

  @Override
  public final Integer[] getIntegerArray() {
    return get();
  }
}