package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.postgres.value.ByteArrayArrayValue;

final class ByteArrayArrayEncoding extends ArrayEncoding<byte[], ByteArrayArrayValue> {

  private final ByteArrayEncoding byteArrayEncoding;
  private final byte[][]          emptyArray = new byte[0][0];

  public ByteArrayArrayEncoding(final ByteArrayEncoding byteArrayEncoding, final Charset charset) {
    super(charset);
    this.byteArrayEncoding = byteArrayEncoding;
  }

  @Override
  public final Integer oid() {
    return Oid.BYTEA_ARRAY;
  }

  @Override
  public final Class<ByteArrayArrayValue> valueClass() {
    return ByteArrayArrayValue.class;
  }

  @Override
  protected byte[][] newArray(final int length) {
    return new byte[length][];
  }

  @Override
  protected byte[][] emptyArray() {
    return emptyArray;
  }

  @Override
  protected Encoding<byte[], ?> itemEncoding() {
    return byteArrayEncoding;
  }

  @Override
  protected ByteArrayArrayValue box(final byte[][] value) {
    return new ByteArrayArrayValue(value);
  }
}
