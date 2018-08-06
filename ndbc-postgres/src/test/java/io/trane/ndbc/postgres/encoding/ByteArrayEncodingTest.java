package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import org.junit.Assert;

import io.trane.ndbc.value.ByteArrayValue;

public class ByteArrayEncodingTest extends EncodingTest<ByteArrayValue, ByteArrayEncoding> {

  public ByteArrayEncodingTest() {
    super(new ByteArrayEncoding(Charset.forName("UTF-8")), Oid.BYTEA, ByteArrayValue.class, r -> {
      final byte[] bytes = new byte[r.nextInt(5)];
      r.nextBytes(bytes);
      return new ByteArrayValue(bytes);
    }, (a, b) -> Assert.assertArrayEquals(a.getByteArray(), b.getByteArray()));
  }

}
