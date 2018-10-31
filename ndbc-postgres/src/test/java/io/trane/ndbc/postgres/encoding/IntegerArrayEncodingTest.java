package io.trane.ndbc.postgres.encoding;

import java.util.Arrays;

import org.junit.Assert;

import io.trane.ndbc.postgres.value.IntegerArrayValue;

public class IntegerArrayEncodingTest extends EncodingTest<IntegerArrayValue, IntegerArrayEncoding> {

  public IntegerArrayEncodingTest() {
    super(new IntegerArrayEncoding(new IntegerEncoding(UTF8), UTF8), Oid.INT4_ARRAY, IntegerArrayValue.class, r -> {
      final Integer[] integers = new Integer[r.nextInt(8)];
      Arrays.setAll(integers, p -> Integer.valueOf(r.nextInt()));
      return new IntegerArrayValue(integers);
    }, (a, b) -> Assert.assertArrayEquals(a.get(), b.get()));
  }
}