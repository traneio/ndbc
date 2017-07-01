package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class IntegerValueTest {

  @Test
  public void getInteger() {
    final Integer value = 32;
    final IntegerValue wrapper = new IntegerValue(value);
    assertEquals(value, wrapper.getInteger());
  }

  @Test
  public void getBigDecimal() {
    final Integer value = 44;
    final IntegerValue wrapper = new IntegerValue(value);
    assertEquals(new BigDecimal(value), wrapper.getBigDecimal());
  }

  @Test
  public void getDouble() {
    final Integer value = 44;
    final IntegerValue wrapper = new IntegerValue(value);
    assertEquals(new Double(value), wrapper.getDouble());
  }

  @Test
  public void getFloat() {
    final Integer value = 44;
    final IntegerValue wrapper = new IntegerValue(value);
    assertEquals(new Float(value), wrapper.getFloat());
  }

  @Test
  public void getLong() {
    final Integer value = 44;
    final IntegerValue wrapper = new IntegerValue(value);
    assertEquals(new Long(value), wrapper.getLong());
  }

  @Test
  public void getBooleanTrue() {
    final Integer value = 1;
    final IntegerValue wrapper = new IntegerValue(value);
    assertEquals(true, wrapper.getBoolean());
  }

  @Test
  public void getBooleanFalse() {
    final Integer value = 0;
    final IntegerValue wrapper = new IntegerValue(value);
    assertEquals(false, wrapper.getBoolean());
  }
}
