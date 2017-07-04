package io.trane.ndbc.postgres.encoding;

import static io.trane.ndbc.util.Collections.toImmutableSet;

import io.trane.ndbc.value.LongValue;

public class LongEncodingTest extends EncodingTest<LongValue, LongEncoding> {

  public LongEncodingTest() {
    super(
        new LongEncoding(),
        toImmutableSet(Oid.INT8),
        LongValue.class,
        r -> new LongValue(r.nextLong()));
  }

}
