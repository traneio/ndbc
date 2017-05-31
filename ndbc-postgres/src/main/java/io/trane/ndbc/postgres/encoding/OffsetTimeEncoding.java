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

class OffsetTimeEncoding implements Encoding<OffsetTimeValue> {

  private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
      .append(DateTimeFormatter.ISO_LOCAL_TIME).optionalStart().appendOffset("+HH:MM:ss", "Z").optionalEnd()
      .optionalStart().appendOffset("+HH:mm", "Z").optionalEnd().optionalStart().appendOffset("+HH", "Z").optionalEnd()
      .toFormatter();

  @Override
  public Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.TIMETZ);
  }

  @Override
  public Class<OffsetTimeValue> valueClass() {
    return OffsetTimeValue.class;
  }

  @Override
  public String encodeText(OffsetTimeValue value) {
    return value.get().toString();
  }

  @Override
  public OffsetTimeValue decodeText(String value) {
    return new OffsetTimeValue(OffsetTime.parse(value, formatter));
  }

  @Override
  public void encodeBinary(OffsetTimeValue value, BufferWriter b) {
    OffsetTime time = value.get();
    b.writeLong(time.toLocalTime().toNanoOfDay() / 1000);
    b.writeInt(time.getOffset().getTotalSeconds());
  }

  @Override
  public OffsetTimeValue decodeBinary(BufferReader b) {
    LocalTime time = LocalTime.ofNanoOfDay(b.readLong() * 1000);
    ZoneOffset zone = ZoneOffset.ofTotalSeconds(-b.readInt());
    return new OffsetTimeValue(time.atOffset(zone));
  }
}
