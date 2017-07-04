package io.trane.ndbc.postgres.encoding;

import static io.trane.ndbc.util.Collections.toImmutableSet;

import io.trane.ndbc.value.BooleanValue;

public class BooleanEncodingTest extends EncodingTest<BooleanValue, BooleanEncoding> {

  public BooleanEncodingTest() {
    super(
        new BooleanEncoding(),
        toImmutableSet(Oid.BOOL),
        BooleanValue.class,
        r -> new BooleanValue(r.nextBoolean()));
  }

}
