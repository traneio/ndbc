package io.trane.ndbc.postgres.encoding;

import java.math.BigDecimal;
import java.nio.charset.Charset;

import io.trane.ndbc.value.BigDecimalValue;

public class BigDecimalEncodingTest extends EncodingTest<BigDecimalValue, BigDecimalEncoding> {

  public BigDecimalEncodingTest() {
    super(new BigDecimalEncoding(Charset.forName("UTF-8")), Oid.NUMERIC, BigDecimalValue.class,
        r -> new BigDecimalValue(BigDecimal.valueOf(r.nextLong(), r.nextInt(100))));
  }

}
