package io.trane.ndbc.value;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NullValueTest {

  @Test
  public void isNull() {
    assertTrue(Value.NULL.isNull());
  }
}
