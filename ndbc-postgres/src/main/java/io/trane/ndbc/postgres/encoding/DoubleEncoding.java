package io.trane.ndbc.postgres.encoding;

import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.DoubleValue;

final class DoubleEncoding extends Encoding<DoubleValue> {

  @Override
  public final Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.FLOAT8);
  }

  @Override
  public final Class<DoubleValue> valueClass() {
    return DoubleValue.class;
  }

  @Override
  public final String encodeText(final DoubleValue value) {
    return Double.toString(value.getDouble());
  }

  @Override
  public final DoubleValue decodeText(final String value) {
    return new DoubleValue(Double.valueOf(value));
  }

  @Override
  public final void encodeBinary(final DoubleValue value, final BufferWriter b) {
    b.writeDouble(value.getDouble());
  }

  @Override
  public final DoubleValue decodeBinary(final BufferReader b) {
    return new DoubleValue(b.readDouble());
  }
}
