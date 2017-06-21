package io.trane.ndbc.postgres.encoding;

import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.LongValue;

final class LongEncoding implements Encoding<LongValue> {

  @Override
  public final Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.INT8);
  }

  @Override
  public final Class<LongValue> valueClass() {
    return LongValue.class;
  }

  @Override
  public final String encodeText(final LongValue value) {
    return Long.toString(value.getLong());
  }

  @Override
  public final LongValue decodeText(final String value) {
    return new LongValue(Long.valueOf(value));
  }

  @Override
  public final void encodeBinary(final LongValue value, final BufferWriter b) {
    b.writeLong(value.getLong());
  }

  @Override
  public final LongValue decodeBinary(final BufferReader b) {
    return new LongValue(b.readLong());
  }
}
