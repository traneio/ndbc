package io.trane.ndbc.postgres.encoding;

import static io.trane.ndbc.util.Collections.toImmutableSet;

import java.time.ZoneOffset;

import io.trane.ndbc.value.OffsetTimeValue;

public class OffsetTimeEncodingTest
    extends EncodingTest<OffsetTimeValue, OffsetTimeEncoding> {

  public OffsetTimeEncodingTest() {
    super(
        new OffsetTimeEncoding(),
        toImmutableSet(Oid.TIMETZ),
        OffsetTimeValue.class,
        r -> new OffsetTimeValue(randomLocalDateTime(r)
            .toLocalTime().atOffset(ZoneOffset.ofTotalSeconds(r.nextInt(18 * 2) + 18))));
  }
}
