package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.time.OffsetTime;

import org.junit.Test;

public class OffsetTimeValueTest {

  @Test
  public void getOffsetTime() {
    final OffsetTime value = OffsetTime.now();
    final OffsetTimeValue wrapper = new OffsetTimeValue(value);
    assertEquals(value, wrapper.getOffsetTime());
  }

  @Test
  public void getLocalTime() {
    final OffsetTime value = OffsetTime.now();
    final OffsetTimeValue wrapper = new OffsetTimeValue(value);
    assertEquals(value.toLocalTime(), wrapper.getLocalTime());
  }
}
