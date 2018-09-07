package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Assert;

import io.trane.ndbc.value.BooleanArrayValue;

public class BooleanArrayEncodingTest extends EncodingTest<BooleanArrayValue, BooleanArrayEncoding> {

  private static final Charset UTF8 = Charset.forName("UTF-8");

  public BooleanArrayEncodingTest() {
    super(new BooleanArrayEncoding(new BooleanEncoding(UTF8), UTF8), Oid.BOOL_ARRAY, BooleanArrayValue.class, r -> {
      final Boolean[] booleans = new Boolean[r.nextInt(8)];
      Arrays.setAll(booleans, g -> r.nextBoolean());
      return new BooleanArrayValue(booleans);
    }, (a, b) -> Assert.assertArrayEquals(a.getBooleanArray(), b.getBooleanArray()));
  }
}
