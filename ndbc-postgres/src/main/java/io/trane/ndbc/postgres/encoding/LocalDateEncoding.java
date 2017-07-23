package io.trane.ndbc.postgres.encoding;

import java.time.LocalDate;
import java.time.temporal.JulianFields;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.LocalDateValue;

final class LocalDateEncoding extends Encoding<LocalDateValue> {

  @Override
  public final Integer oid() {
    return Oid.DATE;
  }

  @Override
  public final Class<LocalDateValue> valueClass() {
    return LocalDateValue.class;
  }

  @Override
  public final String encodeText(final LocalDateValue value) {
    return value.getLocalDate().toString();
  }

  @Override
  public final LocalDateValue decodeText(final String value) {
    return new LocalDateValue(LocalDate.parse(value));
  }

  @Override
  public final void encodeBinary(final LocalDateValue value, final BufferWriter b) {
    b.writeInt((int) value.getLocalDate().getLong(JulianFields.JULIAN_DAY) - 2451545);
  }

  @Override
  public final LocalDateValue decodeBinary(final BufferReader b) {
    return new LocalDateValue(LocalDate.now().with(JulianFields.JULIAN_DAY, b.readInt() + 2451545));
  }
}
