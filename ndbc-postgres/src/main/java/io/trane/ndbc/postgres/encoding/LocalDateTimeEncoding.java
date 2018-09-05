package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.LocalDateTimeValue;

final class LocalDateTimeEncoding extends Encoding<LocalDateTime, LocalDateTimeValue> {

  private static final long POSTGRES_EPOCH_MICROS = 946684800000000L;

  public LocalDateTimeEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public final Integer oid() {
    return Oid.TIMESTAMP;
  }

  @Override
  public final Class<LocalDateTimeValue> valueClass() {
    return LocalDateTimeValue.class;
  }

  @Override
  public final String encodeText(final LocalDateTime value) {
    return java.sql.Timestamp.valueOf(value).toString();
  }

  @Override
  public final LocalDateTime decodeText(final String value) {
    return LocalDateTime.ofInstant(java.sql.Timestamp.valueOf(value).toInstant(), ZoneId.systemDefault());
  }

  @Override
  public final void encodeBinary(final LocalDateTime value, final BufferWriter b) {
    final Instant instant = value.atOffset(ZoneOffset.UTC).toInstant();
    final long seconds = instant.getEpochSecond();
    final long micros = instant.getLong(ChronoField.MICRO_OF_SECOND) + (seconds * 1000000);
    b.writeLong(micros - POSTGRES_EPOCH_MICROS);
  }

  @Override
  public final LocalDateTime decodeBinary(final BufferReader b) {
    final long micros = b.readLong() + POSTGRES_EPOCH_MICROS;
    final long seconds = micros / 1000000L;
    final long nanos = (micros - (seconds * 1000000L)) * 1000;
    final Instant instant = Instant.ofEpochSecond(seconds, nanos);
    final LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    return date;
  }

  @Override
  protected LocalDateTimeValue box(final LocalDateTime value) {
    return new LocalDateTimeValue(value);
  }
}
