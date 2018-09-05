package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.value.DoubleValue;

public class DoubleEncodingTest extends EncodingTest<DoubleValue, DoubleEncoding> {

  public DoubleEncodingTest() {
    super(new DoubleEncoding(Charset.forName("UTF-8")), Oid.FLOAT8, DoubleValue.class,
        r -> new DoubleValue(r.nextDouble()));
  }

}
