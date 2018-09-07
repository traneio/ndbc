package io.trane.ndbc.postgres.encoding;

import org.junit.Assert;

import io.trane.ndbc.value.ByteArrayArrayValue;

public class ByteArrayArrayEncodingTest extends EncodingTest<ByteArrayArrayValue, ByteArrayArrayEncoding> {

  public ByteArrayArrayEncodingTest() {
    super(new ByteArrayArrayEncoding(new ByteArrayEncoding(UTF8), UTF8), Oid.BYTEA_ARRAY, ByteArrayArrayValue.class,
        r -> {
          final byte[][] byteArrays = new byte[r.nextInt(5)][r.nextInt(8)];

          for (final byte[] bs : byteArrays)
            r.nextBytes(bs);

          return new ByteArrayArrayValue(byteArrays);
        }, (a, b) -> Assert.assertArrayEquals(a.getByteArrayArray(), b.getByteArrayArray()));
  }
}