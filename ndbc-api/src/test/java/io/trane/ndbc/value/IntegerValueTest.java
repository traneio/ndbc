package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class IntegerValueTest {

  @Test
  public void getInteger() {
    Integer value = 32;
    IntegerValue wrapper = new IntegerValue(value);
    assertEquals(value, wrapper.getInteger());
  }

  @Test
  public void getBigDecimal() {
    Integer value = 44;
    IntegerValue wrapper = new IntegerValue(value);
    assertEquals(new BigDecimal(value), wrapper.getBigDecimal());
  }

  @Test
  public void getDouble() {
    Integer value = 44;
    IntegerValue wrapper = new IntegerValue(value);
    assertEquals(new Double(value), wrapper.getDouble());
  }

  @Test
  public void getFloat() {
    Integer value = 44;
    IntegerValue wrapper = new IntegerValue(value);
    assertEquals(new Float(value), wrapper.getFloat());
  }

  @Test
  public void getLong() {
    Integer value = 44;
    IntegerValue wrapper = new IntegerValue(value);
    assertEquals(new Long(value), wrapper.getLong());
  }

  @Test
  public void getBooleanTrue() {
    Integer value = 1;
    IntegerValue wrapper = new IntegerValue(value);
    assertEquals(true, wrapper.getBoolean());
  }

  @Test
  public void getBooleanFalse() {
    Integer value = 0;
    IntegerValue wrapper = new IntegerValue(value);
    assertEquals(false, wrapper.getBoolean());
  }
}
