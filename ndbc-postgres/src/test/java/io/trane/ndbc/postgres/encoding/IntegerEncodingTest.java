package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.value.IntegerValue;

public class IntegerEncodingTest extends EncodingTest<IntegerValue, IntegerEncoding> {

  public IntegerEncodingTest() {
    super(new IntegerEncoding(Charset.forName("UTF-8")), Oid.INT4, IntegerValue.class,
        r -> new IntegerValue(r.nextInt()));
  }

}
