package io.trane.ndbc.value;

import java.time.LocalTime;

public class LocalTimeValue extends Value<LocalTime> {

  public LocalTimeValue(LocalTime value) {
    super(value);
  }

  @Override
  public LocalTime getLocalTime() {
    return get();
  }
}
