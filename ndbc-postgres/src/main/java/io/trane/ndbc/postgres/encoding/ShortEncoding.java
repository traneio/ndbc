package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.ShortValue;

class ShortEncoding implements Encoding<ShortValue> {

  @Override
  public String encodeText(ShortValue value) {
    return Short.toString(value.get());
  }

  @Override
  public ShortValue decodeText(String value) {
    return new ShortValue(Short.valueOf(value));
  }

  @Override
  public void encodeBinary(ShortValue value, BufferWriter b) {
    b.writeShort(value.get());
  }

  @Override
  public ShortValue decodeBinary(BufferReader b) {
    return new ShortValue(b.readShort());
  }
}
