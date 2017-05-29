package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.LongValue;

class LongEncoding implements Encoding<LongValue> {

  @Override
  public String encodeText(LongValue value) {
    return Long.toString(value.get());
  }

  @Override
  public LongValue decodeText(String value) {
    return new LongValue(Long.valueOf(value));
  }

  @Override
  public void encodeBinary(LongValue value, BufferWriter b) {
    b.writeLong(value.get());
  }

  @Override
  public LongValue decodeBinary(BufferReader b) {
    return new LongValue(b.readLong());
  }
}
