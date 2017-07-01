package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class DoubleValueTest {

  @Test
  public void getDouble() {
    final Double value = 1.2D;
    final DoubleValue wrapper = new DoubleValue(value);
    assertEquals(value, wrapper.getDouble());
  }

  @Test
  public void getBigDecimal() {
    final Double value = 1.2D;
    final DoubleValue wrapper = new DoubleValue(value);
    assertEquals(new BigDecimal(value), wrapper.getBigDecimal());
  }
}
