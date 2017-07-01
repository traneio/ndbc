package io.trane.ndbc.postgres.encoding;

import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.BooleanValue;

final class BooleanEncoding implements Encoding<BooleanValue> {

  private static final BooleanValue TRUE  = new BooleanValue(true);
  private static final BooleanValue FALSE = new BooleanValue(false);

  @Override
  public final Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.BOOL);
  }

  @Override
  public final Class<BooleanValue> valueClass() {
    return BooleanValue.class;
  }

  @Override
  public final String encodeText(final BooleanValue value) {
    return value.getBoolean() ? "t" : "false";
  }

  @Override
  public final BooleanValue decodeText(final String value) {
    return value == "t" || value == "true" ? TRUE : FALSE;
  }

  @Override
  public final void encodeBinary(final BooleanValue value, final BufferWriter b) {
    b.writeByte((byte) (value.getBoolean() ? 1 : 0));
  }

  @Override
  public final BooleanValue decodeBinary(final BufferReader b) {
    return b.readByte() != 0 ? TRUE : FALSE;
  }

}
