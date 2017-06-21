package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringValueTest {

  @Test
  public void getString() {
    String value = "s";
    StringValue wrapper = new StringValue(value);
    assertEquals(value, wrapper.getString());
  }
}
