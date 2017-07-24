package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.ByteValue;

final class ByteEncoding extends Encoding<ByteValue> {

  @Override
  public final Integer oid() {
    return Oid.INT2;
  }
  
  @Override
  public final Class<ByteValue> valueClass() {
    return ByteValue.class;
  }

  @Override
  public final String encodeText(final ByteValue value) {
    return Byte.toString(value.getByte());
  }

  @Override
  public final ByteValue decodeText(final String value) {
    return new ByteValue(Byte.valueOf(value));
  }

  @Override
  public final void encodeBinary(final ByteValue value, final BufferWriter b) {
    b.writeShort(value.getByte());
  }

  @Override
  public final ByteValue decodeBinary(final BufferReader b) {
    return new ByteValue((byte) b.readShort());
  }
}
