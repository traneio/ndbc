package io.trane.ndbc.postgres.encoding;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.JulianFields;
import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.LocalDateTimeValue;
import io.trane.ndbc.value.LocalDateValue;

class LocalDateTimeEncoding implements Encoding<LocalDateTimeValue> {

  private static final long POSTGRES_EPOCH_MICROS = 946684800000000L;

  @Override
  public Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.TIMESTAMP);
  }

  @Override
  public Class<LocalDateTimeValue> valueClass() {
    return LocalDateTimeValue.class;
  }

  @Override
  public String encodeText(LocalDateTimeValue value) {
    return java.sql.Timestamp.valueOf(value.get()).toString();
  }

  @Override
  public LocalDateTimeValue decodeText(String value) {
    return new LocalDateTimeValue(
        LocalDateTime.ofInstant(java.sql.Timestamp.valueOf(value).toInstant(), ZoneId.systemDefault()));
  }

  @Override
  public void encodeBinary(LocalDateTimeValue value, BufferWriter b) {
    Instant instant = value.get().atOffset(ZoneOffset.UTC).toInstant();
    long seconds = instant.getEpochSecond();
    long micros = instant.getLong(ChronoField.MICRO_OF_SECOND) + seconds * 1000000;
    b.writeLong(micros - POSTGRES_EPOCH_MICROS);
  }

  @Override
  public LocalDateTimeValue decodeBinary(BufferReader b) {
    long micros = b.readLong() + POSTGRES_EPOCH_MICROS;
    long seconds = micros / 1000000L;
    long nanos = (micros - seconds * 1000000L) * 1000;
    Instant instant = Instant.ofEpochSecond(seconds, nanos);
    LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    return new LocalDateTimeValue(date);
  }
}
