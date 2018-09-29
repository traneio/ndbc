package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Set;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.IntegerValue;

final class IntegerEncoding extends Encoding<Integer, IntegerValue> {

  private static final Key signedLongType    = key(FieldTypes.LONG);
  private static final Key unsignedShortType = unsignedKey(FieldTypes.SHORT);
  private static final Key signedInt24Type   = key(FieldTypes.INT24);

  @Override
  public Key key() {
    return signedLongType;
  }

  @Override
  public Set<Key> additionalKeys() {
    return Collections.toImmutableSet(unsignedShortType, signedInt24Type);
  }

  @Override
  public final Class<IntegerValue> valueClass() {
    return IntegerValue.class;
  }

  @Override
  public final Integer decodeText(final String value, Charset charset) {
    return Integer.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Integer value, final PacketBufferWriter b, Charset charset) {
    b.writeInt(Integer.reverseBytes(value));
  }

  @Override
  public final Integer decodeBinary(final PacketBufferReader b, Key key, Charset charset) {
    if (key.equals(unsignedShortType))
      return Integer.reverseBytes(b.readUnsignedShort());
    else
      return Integer.reverseBytes(b.readInt());
  }

  @Override
  protected IntegerValue box(final Integer value) {
    return new IntegerValue(value);
  }
}
