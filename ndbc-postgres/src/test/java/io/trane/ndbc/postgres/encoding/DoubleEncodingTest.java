package io.trane.ndbc.postgres.encoding;

import static io.trane.ndbc.util.Collections.toImmutableSet;

import io.trane.ndbc.value.DoubleValue;

public class DoubleEncodingTest extends EncodingTest<DoubleValue, DoubleEncoding> {

  public DoubleEncodingTest() {
    super(
        new DoubleEncoding(),
        toImmutableSet(Oid.FLOAT8),
        DoubleValue.class,
        r -> new DoubleValue(r.nextDouble()));
  }

}
