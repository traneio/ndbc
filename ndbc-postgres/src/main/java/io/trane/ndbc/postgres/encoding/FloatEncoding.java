package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.FloatValue;

final class FloatEncoding extends Encoding<FloatValue> {

  @Override
  public final Integer oid() {
    return Oid.FLOAT4;
  }

  @Override
  public final Class<FloatValue> valueClass() {
    return FloatValue.class;
  }

  @Override
  public final String encodeText(final FloatValue value) {
    return Float.toString(value.getFloat());
  }

  @Override
  public final FloatValue decodeText(final String value) {
    return new FloatValue(Float.valueOf(value));
  }

  @Override
  public final void encodeBinary(final FloatValue value, final BufferWriter b) {
    b.writeFloat(value.getFloat());
  }

  @Override
  public final FloatValue decodeBinary(final BufferReader b) {
    return new FloatValue(b.readFloat());
  }
}
