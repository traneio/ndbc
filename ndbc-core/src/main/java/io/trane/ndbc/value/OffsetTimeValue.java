package io.trane.ndbc.value;

import java.time.OffsetTime;

public class OffsetTimeValue extends Value<OffsetTime> {

  public OffsetTimeValue(OffsetTime value) {
    super(value);
  }

  @Override
  public OffsetTime getOffsetTime() {
    return get();
  }
}
