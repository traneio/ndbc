package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Set;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.ShortValue;

final class ShortEncoding extends Encoding<Short, ShortValue> {

  public ShortEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public Key key() {
    return key(FieldTypes.SHORT);
  }

  @Override
  public Set<Key> additionalKeys() {
    return Collections.toImmutableSet(unsignedKey(FieldTypes.TINY));
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
    b.writeUnsignedShort(value);
  }

  @Override
  public final Short decodeBinary(final PacketBufferReader b, boolean unsigned) {
    if (unsigned)
      return b.readUnsignedByte();
    else
      return b.readShort();
  }

  @Override
  protected ShortValue box(final Short value) {
    return new ShortValue(value);
  }
}
