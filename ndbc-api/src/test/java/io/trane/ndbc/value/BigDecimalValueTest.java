package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class BigDecimalValueTest {

  @Test
  public void getBigDecimal() {
    final BigDecimal value = new BigDecimal(1.2D);
    final BigDecimalValue wrapper = new BigDecimalValue(value);
    assertEquals(value, wrapper.getBigDecimal());
  }

  @Test
  public void getString() {
    final BigDecimal value = new BigDecimal(1.2D);
    final BigDecimalValue wrapper = new BigDecimalValue(value);
    assertEquals(value.toPlainString(), wrapper.getString());
  }
}
