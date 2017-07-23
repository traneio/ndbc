package io.trane.ndbc.postgres.encoding;

import java.time.LocalTime;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.LocalTimeValue;

final class LocalTimeEncoding extends Encoding<LocalTimeValue> {

  @Override
  public final Integer oid() {
    return Oid.TIME;
  }

  @Override
  public final Class<LocalTimeValue> valueClass() {
    return LocalTimeValue.class;
  }

  @Override
  public final String encodeText(final LocalTimeValue value) {
    return value.getLocalTime().toString();
  }

  @Override
  public final LocalTimeValue decodeText(final String value) {
    return new LocalTimeValue(LocalTime.parse(value));
  }

  @Override
  public final void encodeBinary(final LocalTimeValue value, final BufferWriter b) {
    b.writeLong(value.getLocalTime().toNanoOfDay() / 1000);
  }

  @Override
  public final LocalTimeValue decodeBinary(final BufferReader b) {
    return new LocalTimeValue(LocalTime.ofNanoOfDay(b.readLong() * 1000));
  }
}
