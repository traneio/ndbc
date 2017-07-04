package io.trane.ndbc.postgres.encoding;

import static io.trane.ndbc.util.Collections.toImmutableSet;

import java.math.BigDecimal;

import io.trane.ndbc.value.BigDecimalValue;

public class BigDecimalEncodingTest extends EncodingTest<BigDecimalValue, BigDecimalEncoding> {

  public BigDecimalEncodingTest() {
    super(
        new BigDecimalEncoding(),
        toImmutableSet(Oid.NUMERIC),
        BigDecimalValue.class,
        r -> new BigDecimalValue(BigDecimal.valueOf(r.nextLong(), r.nextInt(100))));
  }

}
