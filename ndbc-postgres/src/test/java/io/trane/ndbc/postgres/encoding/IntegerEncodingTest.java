package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.value.IntegerValue;

public class IntegerEncodingTest extends EncodingTest<IntegerValue, IntegerEncoding> {

  public IntegerEncodingTest() {
    super(new IntegerEncoding(UTF8), Oid.INT4, IntegerValue.class, r -> new IntegerValue(r.nextInt()));
  }

}
