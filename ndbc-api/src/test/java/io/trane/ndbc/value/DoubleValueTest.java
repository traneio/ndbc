package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class DoubleValueTest {

  @Test
  public void getDouble() {
    Double value = 1.2D;
    DoubleValue wrapper = new DoubleValue(value);
    assertEquals(value, wrapper.getDouble());
  }
  
  @Test
  public void getBigDecimal() {
    Double value = 1.2D;
    DoubleValue wrapper = new DoubleValue(value);
    assertEquals(new BigDecimal(value), wrapper.getBigDecimal());
  }
}
