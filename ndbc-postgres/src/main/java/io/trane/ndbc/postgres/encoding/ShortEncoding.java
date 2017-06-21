package io.trane.ndbc.postgres.encoding;

import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.ShortValue;

final class ShortEncoding implements Encoding<ShortValue> {

  @Override
  public final Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.INT2);
  }

  @Override
  public final Class<ShortValue> valueClass() {
    return ShortValue.class;
  }

  @Override
  public final String encodeText(final ShortValue value) {
    return Short.toString(value.getShort());
  }

  @Override
  public final ShortValue decodeText(final String value) {
    return new ShortValue(Short.valueOf(value));
  }

  @Override
  public final void encodeBinary(final ShortValue value, final BufferWriter b) {
    b.writeShort(value.getShort());
  }

  @Override
  public final ShortValue decodeBinary(final BufferReader b) {
    return new ShortValue(b.readShort());
  }
}
