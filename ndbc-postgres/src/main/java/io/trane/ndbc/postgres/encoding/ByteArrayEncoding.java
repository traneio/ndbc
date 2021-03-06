package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.ByteArrayValue;

final class ByteArrayEncoding extends Encoding<byte[], ByteArrayValue> {

  private static final String DEFAULT_PREFIX = "\\x";
  static final String         ARRAY_PREFIX   = "\\\\x";

  private final String prefix;

  public ByteArrayEncoding(final Charset charset) {
    super(charset);
    prefix = DEFAULT_PREFIX;
  }

  public ByteArrayEncoding(final Charset charset, final String prefix) {
    super(charset);
    this.prefix = prefix;
  }

  @Override
  public final Integer oid() {
    return Oid.BYTEA;
  }

  @Override
  public final Class<ByteArrayValue> valueClass() {
    return ByteArrayValue.class;
  }

  @Override
  public final byte[] decodeText(final String value) {
    final char[] chars = value.substring(prefix.length()).toCharArray();
    final byte[] result = new byte[chars.length / 2];
    for (int i = 0; i < result.length; i++)
      result[i] = (byte) Integer.parseInt(String.valueOf(chars, i * 2, 2), 16);
    return result;
  }

  @Override
  public final void encodeBinary(final byte[] value, final BufferWriter b) {
    b.writeBytes(value);
  }

  @Override
  public final byte[] decodeBinary(final BufferReader b) {
    return b.readBytes();
  }

  @Override
  protected ByteArrayValue box(final byte[] value) {
    return new ByteArrayValue(value);
  }
}
