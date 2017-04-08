package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;

class StringEncoding implements Encoding<String> {

  @Override
  public String encodeText(String value) {
    return value;
  }

  @Override
  public String decodeText(String value) {
    return value;
  }

  @Override
  public void encodeBinary(String value, BufferWriter b) {
    b.writeString(value);
  }

  @Override
  public String decodeBinary(BufferReader b) {
    return b.readString();
  }
}
