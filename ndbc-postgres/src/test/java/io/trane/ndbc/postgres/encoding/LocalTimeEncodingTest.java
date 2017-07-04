package io.trane.ndbc.postgres.encoding;

import static io.trane.ndbc.util.Collections.toImmutableSet;

import io.trane.ndbc.value.LocalTimeValue;

public class LocalTimeEncodingTest
    extends EncodingTest<LocalTimeValue, LocalTimeEncoding> {

  public LocalTimeEncodingTest() {
    super(
        new LocalTimeEncoding(),
        toImmutableSet(Oid.TIME),
        LocalTimeValue.class,
        r -> new LocalTimeValue(randomLocalDateTime(r).toLocalTime()));
  }
}
