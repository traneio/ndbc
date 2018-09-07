package io.trane.ndbc.postgres.encoding;

import java.util.Arrays;

import org.junit.Assert;

import io.trane.ndbc.value.ShortArrayValue;

public class ShortArrayEncodingTest extends EncodingTest<ShortArrayValue, ShortArrayEncoding> {

  public ShortArrayEncodingTest() {
    super(new ShortArrayEncoding(new ShortEncoding(UTF8), UTF8), Oid.INT2_ARRAY, ShortArrayValue.class, r -> {
      final Short[] shorts = new Short[r.nextInt(8)];
      Arrays.setAll(shorts, p -> Short.valueOf((short) r.nextInt()));
      return new ShortArrayValue(shorts);
    }, (a, b) -> Assert.assertArrayEquals(a.getShortArray(), b.getShortArray()));
  }
}