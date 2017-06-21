package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class FloatValueTest {

  @Test
  public void getFloat() {
    Float value = 1.2F;
    FloatValue wrapper = new FloatValue(value);
    assertEquals(value, wrapper.getFloat());
  }
  
  @Test
  public void getBigDecimal() {
    Float value = 1.2F;
    FloatValue wrapper = new FloatValue(value);
    assertEquals(new BigDecimal(value), wrapper.getBigDecimal());
  }
}
