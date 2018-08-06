package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.temporal.JulianFields;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.LocalDateValue;

final class LocalDateEncoding extends Encoding<LocalDate, LocalDateValue> {

  public LocalDateEncoding(Charset charset) {
    super(charset);
  }

  @Override
  public final Integer oid() {
    return Oid.DATE;
  }

  @Override
  public final Class<LocalDateValue> valueClass() {
    return LocalDateValue.class;
  }

  @Override
  public final String encodeText(final LocalDate value) {
    return value.toString();
  }

  @Override
  public final LocalDate decodeText(final String value) {
    return LocalDate.parse(value);
  }

  @Override
  public final void encodeBinary(final LocalDate value, final BufferWriter b) {
    b.writeInt((int) value.getLong(JulianFields.JULIAN_DAY) - 2451545);
  }

  @Override
  public final LocalDate decodeBinary(final BufferReader b) {
    return LocalDate.now().with(JulianFields.JULIAN_DAY, b.readInt() + 2451545);
  }

  @Override
  protected LocalDateValue box(final LocalDate value) {
    return new LocalDateValue(value);
  }

  @Override
  protected LocalDate unbox(final LocalDateValue value) {
    return value.getLocalDate();
  }
}
