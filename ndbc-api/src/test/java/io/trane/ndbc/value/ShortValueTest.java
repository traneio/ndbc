package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class ShortValueTest {

  @Test
  public void getShort() {
    Short value = 32;
    ShortValue wrapper = new ShortValue(value);
    assertEquals(value, wrapper.getShort());
  }

  @Test
  public void getInteger() {
    Integer value = 32;
    IntegerValue wrapper = new IntegerValue(value);
    assertEquals(value, wrapper.getInteger());
  }
  
  @Test
  public void getBigDecimal() {
    Short value = 44;
    ShortValue wrapper = new ShortValue(value);
    assertEquals(new BigDecimal(value), wrapper.getBigDecimal());
  }

  @Test
  public void getDouble() {
    Short value = 44;
    ShortValue wrapper = new ShortValue(value);
    assertEquals(new Double(value), wrapper.getDouble());
  }

  @Test
  public void getFloat() {
    Short value = 44;
    ShortValue wrapper = new ShortValue(value);
    assertEquals(new Float(value), wrapper.getFloat());
  }

  @Test
  public void getLong() {
    Short value = 44;
    ShortValue wrapper = new ShortValue(value);
    assertEquals(new Long(value), wrapper.getLong());
  }

  @Test
  public void getBooleanTrue() {
    Short value = 1;
    ShortValue wrapper = new ShortValue(value);
    assertEquals(true, wrapper.getBoolean());
  }

  @Test
  public void getBooleanFalse() {
    Short value = 0;
    ShortValue wrapper = new ShortValue(value);
    assertEquals(false, wrapper.getBoolean());
  }
}
