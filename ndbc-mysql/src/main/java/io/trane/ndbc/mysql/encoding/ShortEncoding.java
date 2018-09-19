package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Set;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.ShortValue;

final class ShortEncoding extends Encoding<Short, ShortValue> {

  private static final Key signedShort  = key(FieldTypes.SHORT);
  private static final Key unsignedTiny = unsignedKey(FieldTypes.TINY);

  public ShortEncoding(final Charset charset) {
    super(charset);
  }

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
  public final Short decodeText(final String value) {
    return Short.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Short value, final PacketBufferWriter b) {
    b.writeShort(Short.reverseBytes(value));
  }

  @Override
  public final Short decodeBinary(final PacketBufferReader b, Key key) {
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
