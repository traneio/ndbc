package io.trane.ndbc.postgres.encoding;

import static io.trane.ndbc.util.Collections.toImmutableSet;

import io.trane.ndbc.value.LocalDateTimeValue;

public class LocalDateTimeEncodingTest
    extends EncodingTest<LocalDateTimeValue, LocalDateTimeEncoding> {

  public LocalDateTimeEncodingTest() {
    super(
        new LocalDateTimeEncoding(),
        toImmutableSet(Oid.TIMESTAMP),
        LocalDateTimeValue.class,
        r -> new LocalDateTimeValue(randomLocalDateTime(r)));
  }
}
