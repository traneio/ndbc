package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.IntegerValue;

final class IntegerEncoding extends Encoding<IntegerValue> {

  @Override
  public final Integer oid() {
    return Oid.INT4;
  }

  @Override
  public final Class<IntegerValue> valueClass() {
    return IntegerValue.class;
  }

  @Override
  public final String encodeText(final IntegerValue value) {
    return Integer.toString(value.getInteger());
  }

  @Override
  public final IntegerValue decodeText(final String value) {
    return new IntegerValue(Integer.valueOf(value));
  }

  @Override
  public final void encodeBinary(final IntegerValue value, final BufferWriter b) {
    b.writeInt(value.getInteger());
  }

  @Override
  public final IntegerValue decodeBinary(final BufferReader b) {
    return new IntegerValue(b.readInt());
  }
}
