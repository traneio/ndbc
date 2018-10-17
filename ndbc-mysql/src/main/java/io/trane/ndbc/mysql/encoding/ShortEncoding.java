package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Set;

import io.trane.ndbc.mysql.proto.FieldType;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.ShortValue;

final class ShortEncoding extends Encoding<Short, ShortValue> {

  private static final Key signedShort  = key(FieldType.SHORT);
  private static final Key unsignedTiny = unsignedKey(FieldType.TINY);

  @Override
  public Key key() {
    return signedShort;
  }

  @Override
  public Set<Key> additionalKeys() {
    return Collections.toImmutableSet(unsignedTiny);
  }

  @Override
  public final Class<ShortValue> valueClass() {
    return ShortValue.class;
  }

  @Override
  public final Short decodeText(final String value, final Charset charset) {
    return Short.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Short value, final PacketBufferWriter b, final Charset charset) {
    b.writeShort(Short.reverseBytes(value));
  }

  @Override
  public final Short decodeBinary(final PacketBufferReader b, final Key key, final Charset charset) {
    if (key.equals(signedShort))
      return Short.reverseBytes(b.readShort());
    else if (key.equals(unsignedTiny))
      return Short.reverseBytes(b.readUnsignedByte());
    else
      throw new IllegalStateException("Invalid key " + key);
  }

  @Override
  protected ShortValue box(final Short value) {
    return new ShortValue(value);
  }
}
