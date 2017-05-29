package io.trane.ndbc.postgres.encoding;

import java.time.LocalDate;
import java.time.temporal.JulianFields;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.LocalDateValue;

class LocalDateEncoding implements Encoding<LocalDateValue> {

  @Override
  public String encodeText(LocalDateValue value) {
    return value.get().toString();
  }

  @Override
  public LocalDateValue decodeText(String value) {
    return new LocalDateValue(LocalDate.parse(value));
  }

  @Override
  public void encodeBinary(LocalDateValue value, BufferWriter b) {
    b.writeInt((int) value.get().getLong(JulianFields.JULIAN_DAY) - 2451545);
  }

  @Override
  public LocalDateValue decodeBinary(BufferReader b) {
    return new LocalDateValue(LocalDate.now().with(JulianFields.JULIAN_DAY, b.readInt() + 2451545));
  }
}
