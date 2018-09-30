package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Set;

import io.trane.ndbc.mysql.proto.FieldType;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.LongValue;

final class LongEncoding extends Encoding<Long, LongValue> {

  private static final Key signedLongLong   = key(FieldType.LONGLONG);
  private static final Key unsignedLong     = unsignedKey(FieldType.LONG);
  private static final Key unsignedLongLong = unsignedKey(FieldType.LONGLONG);

  @Override
  public Key key() {
    return signedLongLong;
  }

  @Override
  public Set<Key> additionalKeys() {
    return Collections.toImmutableSet(unsignedLong, unsignedLongLong);
  }

  @Override
  public final Class<LongValue> valueClass() {
    return LongValue.class;
  }

  @Override
  public final Long decodeText(final String value, Charset charset) {
    return Long.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Long value, final PacketBufferWriter b, Charset charset) {
    b.writeLong(Long.reverseBytes(value));
  }

  @Override
  public final Long decodeBinary(final PacketBufferReader b, Key key, Charset charset) {
    if (key.equals(signedLongLong))
      return Long.reverseBytes(b.readLong());
    else if (key.equals(unsignedLong))
      return Long.reverseBytes(b.readUnsignedInt());
    else if (key.equals(unsignedLongLong))
      return b.readUnsignedLongLE().longValue();
    else
      throw new IllegalStateException("Invalid key " + key);
  }

  @Override
  protected LongValue box(final Long value) {
    return new LongValue(value);
  }
}
