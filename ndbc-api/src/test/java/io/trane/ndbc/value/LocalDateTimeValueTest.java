package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

public class LocalDateTimeValueTest {

  @Test
  public void getLocalDateTime() {
    LocalDateTime value = LocalDateTime.now();
    LocalDateTimeValue wrapper = new LocalDateTimeValue(value);
    assertEquals(value, wrapper.getLocalDateTime());
  }
  
  @Test
  public void getLocalDate() {
    LocalDateTime value = LocalDateTime.now();
    LocalDateTimeValue wrapper = new LocalDateTimeValue(value);
    assertEquals(value.toLocalDate(), wrapper.getLocalDate());
  }
  
  @Test
  public void getLocalTime() {
    LocalDateTime value = LocalDateTime.now();
    LocalDateTimeValue wrapper = new LocalDateTimeValue(value);
    assertEquals(value.toLocalTime(), wrapper.getLocalTime());
  }
}
