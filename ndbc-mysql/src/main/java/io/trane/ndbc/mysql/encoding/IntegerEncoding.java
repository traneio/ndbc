package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Set;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.IntegerValue;

final class IntegerEncoding extends Encoding<Integer, IntegerValue> {

  public IntegerEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public Key key() {
    return key(FieldTypes.INT24);
  }

  @Override
  public Set<Key> additionalKeys() {
    return Collections.toImmutableSet(unsignedKey(FieldTypes.SHORT), key(FieldTypes.LONG));
  }

  @Override
  public final Class<IntegerValue> valueClass() {
    return IntegerValue.class;
  }

  @Override
  public final Integer decodeText(final String value) {
    return Integer.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Integer value, final PacketBufferWriter b) {
    b.writeInt(Integer.reverseBytes(value));
  }

  @Override
  public final Integer decodeBinary(final PacketBufferReader b, Key key) {
    return Integer.reverseBytes(b.readInt());
  }

  @Override
  protected IntegerValue box(final Integer value) {
    return new IntegerValue(value);
  }
}
