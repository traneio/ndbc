package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.value.LocalTimeValue;

public class LocalTimeEncodingTest extends EncodingTest<LocalTimeValue, LocalTimeEncoding> {

  public LocalTimeEncodingTest() {
    super(new LocalTimeEncoding(Charset.forName("UTF-8")), Oid.TIME, LocalTimeValue.class,
        r -> new LocalTimeValue(randomLocalDateTime(r).toLocalTime()));
  }
}
