package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CharacterValueTest {

  @Test
  public void getCharacter() {
    Character value = 'j';
    CharacterValue wrapper = new CharacterValue(value);
    assertEquals(value, wrapper.getCharacter());
  }

  @Test
  public void getString() {
    Character value = 'j';
    CharacterValue wrapper = new CharacterValue(value);
    assertEquals(value.toString(), wrapper.getString());
  }
  
  @Test
  public void getBooleanTrue1() {
    Character value = '1';
    CharacterValue wrapper = new CharacterValue(value);
    assertEquals(true, wrapper.getBoolean());
  }
  
  @Test
  public void getBooleanTrueT() {
    Character value = 'T';
    CharacterValue wrapper = new CharacterValue(value);
    assertEquals(true, wrapper.getBoolean());
  }
  
  @Test
  public void getBooleanFalse0() {
    Character value = '0';
    CharacterValue wrapper = new CharacterValue(value);
    assertEquals(false, wrapper.getBoolean());
  }
  
  @Test
  public void getBooleanFalseF() {
    Character value = 'F';
    CharacterValue wrapper = new CharacterValue(value);
    assertEquals(false, wrapper.getBoolean());
  }
}
