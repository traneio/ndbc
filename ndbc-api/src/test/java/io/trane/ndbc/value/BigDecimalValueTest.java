package io.trane.ndbc.value;

import java.math.BigDecimal;

import org.junit.Test;
import static org.junit.Assert.*;

public class BigDecimalValueTest {

  @Test
  public void getBigDecimal() {
    BigDecimal value = new BigDecimal(1.2D);
    BigDecimalValue wrapper = new BigDecimalValue(value);
    assertEquals(value, wrapper.getBigDecimal());
  }

  @Test
  public void getString() {
    BigDecimal value = new BigDecimal(1.2D);
    BigDecimalValue wrapper = new BigDecimalValue(value);
    assertEquals(value.toPlainString(), wrapper.getString());
  }
}
