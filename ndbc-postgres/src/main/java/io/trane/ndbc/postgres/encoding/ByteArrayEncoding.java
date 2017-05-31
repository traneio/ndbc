package io.trane.ndbc.postgres.encoding;

import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.ByteArrayValue;

public class ByteArrayEncoding implements Encoding<ByteArrayValue> {

  private static final String PREFIX = "\\x";

  @Override
  public Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.BYTEA);
  }
  
  @Override
  public Class<ByteArrayValue> valueClass() {
    return ByteArrayValue.class;
  }
  
  @Override
  public String encodeText(ByteArrayValue value) {
    StringBuilder sb = new StringBuilder();
    sb.append(PREFIX);
    for (byte b : value.get())
      sb.append(String.format("%02x", b));
    return sb.toString();
  }

  @Override
  public ByteArrayValue decodeText(String value) {
    assert (value.startsWith(PREFIX));
    char[] chars = value.substring(PREFIX.length()).toCharArray();
    byte[] result = new byte[chars.length / 2];
    for (int i = 0; i < result.length; i++)
      result[i] = (byte) Integer.parseInt(String.valueOf(chars, i * 2, 2), 16);
    return new ByteArrayValue(result);
  }

  @Override
  public void encodeBinary(ByteArrayValue value, BufferWriter b) {
    b.writeBytes(value.get());
  }

  @Override
  public ByteArrayValue decodeBinary(BufferReader b) {
    return new ByteArrayValue(b.readBytes());
  }
}
