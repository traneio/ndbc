package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.value.LongValue;

public class LongEncodingTest extends EncodingTest<LongValue, LongEncoding> {

  public LongEncodingTest() {
    super(new LongEncoding(Charset.forName("UTF-8")), Oid.INT8, LongValue.class, r -> new LongValue(r.nextLong()));
  }

}
