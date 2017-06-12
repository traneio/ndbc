package io.trane.ndbc.value;

import java.time.OffsetTime;

public final class OffsetTimeValue extends Value<OffsetTime> {

  public OffsetTimeValue(final OffsetTime value) {
    super(value);
  }

  @Override
  public final OffsetTime getOffsetTime() {
    return get();
  }
}
