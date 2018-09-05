package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

public class LocalDateTimeValueTest {

  @Test
  public void getLocalDateTime() {
    final LocalDateTime value = LocalDateTime.now();
    final LocalDateTimeValue wrapper = new LocalDateTimeValue(value);
    assertEquals(value, wrapper.getLocalDateTime());
  }

  @Test
  public void getLocalDate() {
    final LocalDateTime value = LocalDateTime.now();
    final LocalDateTimeValue wrapper = new LocalDateTimeValue(value);
    assertEquals(value.toLocalDate(), wrapper.getLocalDate());
  }

  @Test
  public void getLocalTime() {
    final LocalDateTime value = LocalDateTime.now();
    final LocalDateTimeValue wrapper = new LocalDateTimeValue(value);
    assertEquals(value.toLocalTime(), wrapper.getLocalTime());
  }
}
