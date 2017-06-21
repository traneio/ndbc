package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.time.OffsetTime;

import org.junit.Test;

public class OffsetTimeValueTest {

  @Test
  public void getOffsetTime() {
    OffsetTime value = OffsetTime.now();
    OffsetTimeValue wrapper = new OffsetTimeValue(value);
    assertEquals(value, wrapper.getOffsetTime());
  }

  @Test
  public void getLocalTime() {
    OffsetTime value = OffsetTime.now();
    OffsetTimeValue wrapper = new OffsetTimeValue(value);
    assertEquals(value.toLocalTime(), wrapper.getLocalTime());
  }
}
