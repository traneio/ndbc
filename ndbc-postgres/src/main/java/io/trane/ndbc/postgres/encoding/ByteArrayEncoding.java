package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.ByteArrayValue;

final class ByteArrayEncoding extends Encoding<ByteArrayValue> {

  private static final String PREFIX = "\\x";

  @Override
  public final Integer oid() {
    return Oid.BYTEA;
  }

  @Override
  public final Class<ByteArrayValue> valueClass() {
    return ByteArrayValue.class;
  }

  @Override
  public final String encodeText(final ByteArrayValue value) {
    final StringBuilder sb = new StringBuilder();
    sb.append(PREFIX);
    for (final byte b : value.getByteArray())
      sb.append(String.format("%02x", b));
    return sb.toString();
  }

  @Override
  public final ByteArrayValue decodeText(final String value) {
    final char[] chars = value.substring(PREFIX.length()).toCharArray();
    final byte[] result = new byte[chars.length / 2];
    for (int i = 0; i < result.length; i++)
      result[i] = (byte) Integer.parseInt(String.valueOf(chars, i * 2, 2), 16);
    return new ByteArrayValue(result);
  }

  @Override
  public final void encodeBinary(final ByteArrayValue value, final BufferWriter b) {
    b.writeBytes(value.getByteArray());
  }

  @Override
  public final ByteArrayValue decodeBinary(final BufferReader b) {
    return new ByteArrayValue(b.readBytes());
  }
}
