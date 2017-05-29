package io.trane.ndbc.postgres.encoding;

import java.time.LocalTime;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.LocalTimeValue;

class LocalTimeEncoding implements Encoding<LocalTimeValue> {

  @Override
  public String encodeText(LocalTimeValue value) {
    return value.get().toString();
  }

  @Override
  public LocalTimeValue decodeText(String value) {
    return new LocalTimeValue(LocalTime.parse(value));
  }

  @Override
  public void encodeBinary(LocalTimeValue value, BufferWriter b) {
    b.writeLong(value.get().toNanoOfDay() / 1000);
  }

  @Override
  public LocalTimeValue decodeBinary(BufferReader b) {
    return new LocalTimeValue(LocalTime.ofNanoOfDay(b.readLong() * 1000));
  }
}
