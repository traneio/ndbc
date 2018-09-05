package io.trane.ndbc.value;

import java.time.OffsetTime;

public final class OffsetTimeArrayValue extends Value<OffsetTime[]> {

  public OffsetTimeArrayValue(final OffsetTime[] value) {
    super(value);
  }

  @Override
  public final OffsetTime[] getOffsetTimeArray() {
    return get();
  }
}