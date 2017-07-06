package io.trane.ndbc.postgres.encoding;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.OffsetTimeValue;

final class OffsetTimeEncoding extends Encoding<OffsetTimeValue> {

  private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
      .parseCaseInsensitive()
      .append(DateTimeFormatter.ISO_LOCAL_TIME).optionalStart().appendOffset("+HH:MM:ss", "Z")
      .optionalEnd()
      .optionalStart().appendOffset("+HH:mm", "Z").optionalEnd().optionalStart()
      .appendOffset("+HH", "Z").optionalEnd()
      .toFormatter();

  @Override
  public final Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.TIMETZ);
  }

  @Override
  public final Class<OffsetTimeValue> valueClass() {
    return OffsetTimeValue.class;
  }

  @Override
  public final String encodeText(final OffsetTimeValue value) {
    return value.getOffsetTime().toString();
  }

  @Override
  public final OffsetTimeValue decodeText(final String value) {
    return new OffsetTimeValue(OffsetTime.parse(value, formatter));
  }

  @Override
  public final void encodeBinary(final OffsetTimeValue value, final BufferWriter b) {
    final OffsetTime time = value.getOffsetTime();
    b.writeLong(time.toLocalTime().toNanoOfDay() / 1000);
    b.writeInt(-time.getOffset().getTotalSeconds());
  }

  @Override
  public final OffsetTimeValue decodeBinary(final BufferReader b) {
    final LocalTime time = LocalTime.ofNanoOfDay(b.readLong() * 1000);
    final ZoneOffset zone = ZoneOffset.ofTotalSeconds(-b.readInt());
    return new OffsetTimeValue(time.atOffset(zone));
  }
}
