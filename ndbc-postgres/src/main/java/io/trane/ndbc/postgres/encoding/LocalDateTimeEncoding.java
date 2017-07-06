package io.trane.ndbc.postgres.encoding;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.LocalDateTimeValue;

final class LocalDateTimeEncoding extends Encoding<LocalDateTimeValue> {

  private static final long POSTGRES_EPOCH_MICROS = 946684800000000L;

  @Override
  public final Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.TIMESTAMP);
  }

  @Override
  public final Class<LocalDateTimeValue> valueClass() {
    return LocalDateTimeValue.class;
  }

  @Override
  public final String encodeText(final LocalDateTimeValue value) {
    return java.sql.Timestamp.valueOf(value.getLocalDateTime()).toString();
  }

  @Override
  public final LocalDateTimeValue decodeText(final String value) {
    return new LocalDateTimeValue(
        LocalDateTime.ofInstant(java.sql.Timestamp.valueOf(value).toInstant(),
            ZoneId.systemDefault()));
  }

  @Override
  public final void encodeBinary(final LocalDateTimeValue value, final BufferWriter b) {
    final Instant instant = value.getLocalDateTime().atOffset(ZoneOffset.UTC).toInstant();
    final long seconds = instant.getEpochSecond();
    final long micros = instant.getLong(ChronoField.MICRO_OF_SECOND) + seconds * 1000000;
    b.writeLong(micros - POSTGRES_EPOCH_MICROS);
  }

  @Override
  public final LocalDateTimeValue decodeBinary(final BufferReader b) {
    final long micros = b.readLong() + POSTGRES_EPOCH_MICROS;
    final long seconds = micros / 1000000L;
    final long nanos = (micros - seconds * 1000000L) * 1000;
    final Instant instant = Instant.ofEpochSecond(seconds, nanos);
    final LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    return new LocalDateTimeValue(date);
  }
}
