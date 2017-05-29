package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.DoubleValue;

class DoubleEncoding implements Encoding<DoubleValue> {

  @Override
  public String encodeText(DoubleValue value) {
    return Double.toString(value.get());
  }

  @Override
  public DoubleValue decodeText(String value) {
    return new DoubleValue(Double.valueOf(value));
  }

  @Override
  public void encodeBinary(DoubleValue value, BufferWriter b) {
    b.writeDouble(value.get());
  }

  @Override
  public DoubleValue decodeBinary(BufferReader b) {
    return new DoubleValue(b.readDouble());
  }
}
