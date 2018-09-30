package io.trane.ndbc.postgres.value;

import io.trane.ndbc.value.Value;

public final class BooleanArrayValue extends Value<Boolean[]> {

  public BooleanArrayValue(final Boolean[] value) {
    super(value);
  }

  @Override
  public final Boolean[] getBooleanArray() {
    return get();
  }
}
