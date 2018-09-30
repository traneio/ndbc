package io.trane.ndbc.postgres.value;

import java.time.LocalTime;
import java.time.OffsetTime;

import io.trane.ndbc.value.Value;

public final class OffsetTimeValue extends Value<OffsetTime> {

  public OffsetTimeValue(final OffsetTime value) {
    super(value);
  }

  @Override
  public final OffsetTime getOffsetTime() {
    return get();
  }

  @Override
  public final LocalTime getLocalTime() {
    return get().toLocalTime();
  }
}
