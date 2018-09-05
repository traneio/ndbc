package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.time.LocalTime;

import org.junit.Test;

public class LocalTimeValueTest {

  @Test
  public void getLocalTime() {
    final LocalTime value = LocalTime.now();
    final LocalTimeValue wrapper = new LocalTimeValue(value);
    assertEquals(value, wrapper.getLocalTime());
  }
}
