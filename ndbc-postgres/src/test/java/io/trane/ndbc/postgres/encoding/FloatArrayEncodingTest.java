package io.trane.ndbc.postgres.encoding;

import java.util.Arrays;

import org.junit.Assert;

import io.trane.ndbc.value.FloatArrayValue;

public class FloatArrayEncodingTest extends EncodingTest<FloatArrayValue, FloatArrayEncoding> {

  public FloatArrayEncodingTest() {
    super(new FloatArrayEncoding(new FloatEncoding(UTF8), UTF8), Oid.FLOAT4_ARRAY, FloatArrayValue.class, r -> {
      final Float[] floats = new Float[r.nextInt(8)];
      Arrays.setAll(floats, p -> Float.valueOf(r.nextFloat()));
      return new FloatArrayValue(floats);
    }, (a, b) -> Assert.assertArrayEquals(a.getFloatArray(), b.getFloatArray()));
  }
}