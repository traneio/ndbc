package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.value.LocalTimeValue;

public class LocalTimeEncodingTest extends EncodingTest<LocalTimeValue, LocalTimeEncoding> {

  public LocalTimeEncodingTest() {
    super(new LocalTimeEncoding(UTF8), Oid.TIME, LocalTimeValue.class,
        r -> new LocalTimeValue(randomLocalDateTime(r).toLocalTime()));
  }
}
