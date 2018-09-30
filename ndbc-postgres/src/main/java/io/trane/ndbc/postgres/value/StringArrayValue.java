package io.trane.ndbc.postgres.value;

import io.trane.ndbc.value.Value;

public final class StringArrayValue extends Value<String[]> {

  public StringArrayValue(final String[] value) {
    super(value);
  }

  @Override
  public final String[] getStringArray() {
    return get();
  }
}