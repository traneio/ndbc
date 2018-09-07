package io.trane.ndbc.postgres.encoding;

import java.util.Arrays;

import org.junit.Assert;

import io.trane.ndbc.value.DoubleArrayValue;

public class DoubleArrayEncodingTest extends EncodingTest<DoubleArrayValue, DoubleArrayEncoding> {

  public DoubleArrayEncodingTest() {
    super(new DoubleArrayEncoding(new DoubleEncoding(UTF8), UTF8), Oid.FLOAT8_ARRAY, DoubleArrayValue.class, r -> {
      final Double[] doubles = new Double[r.nextInt(8)];
      Arrays.setAll(doubles, p -> Double.valueOf(r.nextDouble()));
      return new DoubleArrayValue(doubles);
    }, (a, b) -> Assert.assertArrayEquals(a.getDoubleArray(), b.getDoubleArray()));
  }
}