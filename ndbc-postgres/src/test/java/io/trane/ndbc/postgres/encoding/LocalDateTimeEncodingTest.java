package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.value.LocalDateTimeValue;

public class LocalDateTimeEncodingTest extends EncodingTest<LocalDateTimeValue, LocalDateTimeEncoding> {

  public LocalDateTimeEncodingTest() {
    super(new LocalDateTimeEncoding(Charset.forName("UTF-8")), Oid.TIMESTAMP, LocalDateTimeValue.class,
        r -> new LocalDateTimeValue(randomLocalDateTime(r)));
  }
}
