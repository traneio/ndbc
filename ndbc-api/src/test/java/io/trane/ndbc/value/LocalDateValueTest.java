package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

public class LocalDateValueTest {

  @Test
  public void getLocalDate() {
    final LocalDate value = LocalDate.now();
    final LocalDateValue wrapper = new LocalDateValue(value);
    assertEquals(value, wrapper.getLocalDate());
  }
}
