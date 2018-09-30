package io.trane.ndbc.postgres.value;

import java.time.OffsetTime;

import io.trane.ndbc.value.Value;

public final class OffsetTimeArrayValue extends Value<OffsetTime[]> {

  public OffsetTimeArrayValue(final OffsetTime[] value) {
    super(value);
  }

  @Override
  public final OffsetTime[] getOffsetTimeArray() {
    return get();
  }
}