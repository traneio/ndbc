package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.IntegerValue;

class IntegerEncoding implements Encoding<IntegerValue> {

  @Override
  public String encodeText(IntegerValue value) {
    return Integer.toString(value.get());
  }

  @Override
  public IntegerValue decodeText(String value) {
    return new IntegerValue(Integer.valueOf(value));
  }

  @Override
  public void encodeBinary(IntegerValue value, BufferWriter b) {
    b.writeInt(value.get());
  }

  @Override
  public IntegerValue decodeBinary(BufferReader b) {
    return new IntegerValue(b.readInt());
  }
}
