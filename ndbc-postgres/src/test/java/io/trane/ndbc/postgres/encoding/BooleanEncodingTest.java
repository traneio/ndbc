package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.value.BooleanValue;

public class BooleanEncodingTest extends EncodingTest<BooleanValue, BooleanEncoding> {

  public BooleanEncodingTest() {
    super(new BooleanEncoding(UTF8), Oid.BOOL, BooleanValue.class, r -> new BooleanValue(r.nextBoolean()));
  }
}
