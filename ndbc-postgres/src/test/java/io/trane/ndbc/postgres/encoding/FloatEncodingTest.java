package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.value.FloatValue;

public class FloatEncodingTest extends EncodingTest<FloatValue, FloatEncoding> {

  public FloatEncodingTest() {
    super(
        new FloatEncoding(),
        Oid.FLOAT4,
        FloatValue.class,
        r -> new FloatValue(r.nextFloat()));
  }

}
