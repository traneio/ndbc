package io.trane.ndbc.postgres.value;

import io.trane.ndbc.value.Value;

public final class ShortArrayValue extends Value<Short[]> {

  public static final ShortArrayValue EMPTY = new ShortArrayValue(new Short[0]);

  public ShortArrayValue(final Short[] value) {
    super(value);
  }

  @Override
  public final Short[] getShortArray() {
    return get();
  }
}