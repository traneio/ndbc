package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BooleanValueTest {

  @Test
  public void getBoolean() {
    Boolean value = true;
    BooleanValue wrapper = new BooleanValue(value);
    assertEquals(value, wrapper.getBoolean());
  }
  
  @Test
  public void getStringTrue() {
    Boolean value = true;
    BooleanValue wrapper = new BooleanValue(value);
    assertEquals("T", wrapper.getString());
  }

  @Test
  public void getStringFalse() {
    Boolean value = false;
    BooleanValue wrapper = new BooleanValue(value);
    assertEquals("F", wrapper.getString());
  }

  @Test
  public void getBooleanFalse() {
    Boolean value = false;
    BooleanValue wrapper = new BooleanValue(value);
    assertEquals(value, wrapper.getBoolean());
  }

  @Test
  public void getIntegerTrue() {
    Boolean value = true;
    BooleanValue wrapper = new BooleanValue(value);
    assertEquals(new Integer(1), wrapper.getInteger());
  }

  @Test
  public void getIntegerFalse() {
    Boolean value = false;
    BooleanValue wrapper = new BooleanValue(value);
    assertEquals(new Integer(0), wrapper.getInteger());
  }

  @Test
  public void getShortTrue() {
    Boolean value = true;
    BooleanValue wrapper = new BooleanValue(value);
    assertEquals(new Short((short) 1), wrapper.getShort());
  }

  @Test
  public void getShortFalse() {
    Boolean value = false;
    BooleanValue wrapper = new BooleanValue(value);
    assertEquals(new Short((short) 0), wrapper.getShort());
  }

  @Test
  public void getCharacterTrue() {
    Boolean value = true;
    BooleanValue wrapper = new BooleanValue(value);
    assertEquals(new Character('T'), wrapper.getCharacter());
  }

  @Test
  public void getCharacterFalse() {
    Boolean value = false;
    BooleanValue wrapper = new BooleanValue(value);
    assertEquals(new Character('F'), wrapper.getCharacter());
  }
}
