package io.trane.ndbc.postgres.encoding;

import static io.trane.ndbc.util.Collections.toImmutableSet;

import io.trane.ndbc.value.FloatValue;

public class FloatEncodingTest extends EncodingTest<FloatValue, FloatEncoding> {

  public FloatEncodingTest() {
    super(
        new FloatEncoding(),
        toImmutableSet(Oid.FLOAT4),
        FloatValue.class,
        r -> new FloatValue(r.nextFloat()));
  }

}
