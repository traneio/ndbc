package io.trane.ndbc.postgres.encoding;

import java.util.Arrays;

import org.junit.Assert;

import io.trane.ndbc.value.LongArrayValue;

public class LongArrayEncodingTest extends EncodingTest<LongArrayValue, LongArrayEncoding> {

  public LongArrayEncodingTest() {
    super(new LongArrayEncoding(new LongEncoding(UTF8), UTF8), Oid.INT8_ARRAY, LongArrayValue.class, r -> {
      final Long[] longs = new Long[r.nextInt(8)];
      Arrays.setAll(longs, p -> Long.valueOf(r.nextLong()));
      return new LongArrayValue(longs);
    }, (a, b) -> Assert.assertArrayEquals(a.getLongArray(), b.getLongArray()));
  }
}