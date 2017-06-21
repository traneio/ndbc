package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class LongValueTest {

  @Test
  public void getLong() {
    Long value = 32L;
    LongValue wrapper = new LongValue(value);
    assertEquals(value, wrapper.getLong());
  }

  @Test
  public void getBigDecimal() {
    Long value = 44L;
    LongValue wrapper = new LongValue(value);
    assertEquals(new BigDecimal(value), wrapper.getBigDecimal());
  }
}
