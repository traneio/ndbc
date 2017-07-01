package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BooleanValueTest {

  @Test
  public void getBoolean() {
    final Boolean value = true;
    final BooleanValue wrapper = new BooleanValue(value);
    assertEquals(value, wrapper.getBoolean());
  }

  @Test
  public void getStringTrue() {
    final Boolean value = true;
    final BooleanValue wrapper = new BooleanValue(value);
    assertEquals("T", wrapper.getString());
  }

  @Test
  public void getStringFalse() {
    final Boolean value = false;
    final BooleanValue wrapper = new BooleanValue(value);
    assertEquals("F", wrapper.getString());
  }

  @Test
  public void getBooleanFalse() {
    final Boolean value = false;
    final BooleanValue wrapper = new BooleanValue(value);
    assertEquals(value, wrapper.getBoolean());
  }

  @Test
  public void getIntegerTrue() {
    final Boolean value = true;
    final BooleanValue wrapper = new BooleanValue(value);
    assertEquals(new Integer(1), wrapper.getInteger());
  }

  @Test
  public void getIntegerFalse() {
    final Boolean value = false;
    final BooleanValue wrapper = new BooleanValue(value);
    assertEquals(new Integer(0), wrapper.getInteger());
  }

  @Test
  public void getShortTrue() {
    final Boolean value = true;
    final BooleanValue wrapper = new BooleanValue(value);
    assertEquals(new Short((short) 1), wrapper.getShort());
  }

  @Test
  public void getShortFalse() {
    final Boolean value = false;
    final BooleanValue wrapper = new BooleanValue(value);
    assertEquals(new Short((short) 0), wrapper.getShort());
  }

  @Test
  public void getCharacterTrue() {
    final Boolean value = true;
    final BooleanValue wrapper = new BooleanValue(value);
    assertEquals(new Character('T'), wrapper.getCharacter());
  }

  @Test
  public void getCharacterFalse() {
    final Boolean value = false;
    final BooleanValue wrapper = new BooleanValue(value);
    assertEquals(new Character('F'), wrapper.getCharacter());
  }
}
