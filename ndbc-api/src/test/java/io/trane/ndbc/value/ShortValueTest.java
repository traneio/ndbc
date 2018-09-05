package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class ShortValueTest {

  @Test
  public void getShort() {
    final Short value = 32;
    final ShortValue wrapper = new ShortValue(value);
    assertEquals(value, wrapper.getShort());
  }

  @Test
  public void getInteger() {
    final Integer value = 32;
    final IntegerValue wrapper = new IntegerValue(value);
    assertEquals(value, wrapper.getInteger());
  }

  @Test
  public void getBigDecimal() {
    final Short value = 44;
    final ShortValue wrapper = new ShortValue(value);
    assertEquals(new BigDecimal(value), wrapper.getBigDecimal());
  }

  @Test
  public void getDouble() {
    final Short value = 44;
    final ShortValue wrapper = new ShortValue(value);
    assertEquals(new Double(value), wrapper.getDouble());
  }

  @Test
  public void getFloat() {
    final Short value = 44;
    final ShortValue wrapper = new ShortValue(value);
    assertEquals(new Float(value), wrapper.getFloat());
  }

  @Test
  public void getLong() {
    final Short value = 44;
    final ShortValue wrapper = new ShortValue(value);
    assertEquals(new Long(value), wrapper.getLong());
  }

  @Test
  public void getBooleanTrue() {
    final Short value = 1;
    final ShortValue wrapper = new ShortValue(value);
    assertEquals(true, wrapper.getBoolean());
  }

  @Test
  public void getBooleanFalse() {
    final Short value = 0;
    final ShortValue wrapper = new ShortValue(value);
    assertEquals(false, wrapper.getBoolean());
  }
}
