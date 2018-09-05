package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringValueTest {

  @Test
  public void getString() {
    final String value = "s";
    final StringValue wrapper = new StringValue(value);
    assertEquals(value, wrapper.getString());
  }
}
