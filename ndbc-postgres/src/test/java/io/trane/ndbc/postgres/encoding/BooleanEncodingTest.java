package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.value.BooleanValue;

public class BooleanEncodingTest extends EncodingTest<BooleanValue, BooleanEncoding> {

  public BooleanEncodingTest() {
    super(new BooleanEncoding(Charset.forName("UTF-8")), Oid.BOOL, BooleanValue.class,
        r -> new BooleanValue(r.nextBoolean()));
  }

}
