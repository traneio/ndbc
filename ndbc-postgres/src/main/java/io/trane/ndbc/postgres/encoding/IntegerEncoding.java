package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;

class IntegerEncoding implements Encoding<Integer> {

  @Override
  public String encodeText(Integer value) {
    return Integer.toString(value);
  }

  @Override
  public Integer decodeText(String value) {
    return Integer.valueOf(value);
  }

  @Override
  public void encodeBinary(Integer value, BufferWriter b) {
    b.writeInt(value);
  }

  @Override
  public Integer decodeBinary(BufferReader b) {
    return b.readInt();
  }
}
