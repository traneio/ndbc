package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

import io.trane.ndbc.mysql.proto.FieldType;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.LocalTimeValue;

final class LocalTimeEncoding extends Encoding<LocalTime, LocalTimeValue> {

  private static final DateTimeFormatter withNanos = new DateTimeFormatterBuilder()
      .appendPattern("HH:mm:ss.SSSSSS")
      .toFormatter();

  private static final DateTimeFormatter withoutNanos = new DateTimeFormatterBuilder()
      .appendPattern("HH:mm:ss")
      .toFormatter();

  @Override
  public Key key() {
    return key(FieldType.TIME);
  }

  @Override
  public final Class<LocalTimeValue> valueClass() {
    return LocalTimeValue.class;
  }

  @Override
  public final LocalTime decodeText(final String value, Charset charset) {
    try {
      return LocalTime.parse(value, withNanos);
    } catch (DateTimeParseException ex) {
      return LocalTime.parse(value, withoutNanos);
    }
  }

  @Override
  public final void encodeBinary(final LocalTime value, final PacketBufferWriter b, Charset charset) {
    b.writeByte((byte) 12);
    b.writeByte((byte) 0); // never negative
    b.writeInt(0); // days
    b.writeByte((byte) value.getHour());
    b.writeByte((byte) value.getMinute());
    b.writeByte((byte) value.getSecond());
    b.writeInt(Integer.reverseBytes(value.getNano() / 1000));
  }

  @Override
  public final LocalTime decodeBinary(final PacketBufferReader b, Key key, Charset charset) {
    byte length = b.readByte();
    assert (b.readByte() == (byte) 0);
    assert (b.readInt() == (byte) 0);
    byte hour = b.readByte();
    byte minute = b.readByte();
    byte second = b.readByte();

    switch (length) {
      case 8:
        return LocalTime.of(hour, minute, second);
      case 12:
        int nanos = Integer.reverseBytes(b.readInt()) * 1000;
        return LocalTime.of(hour, minute, second, nanos);
      default:
        throw new IllegalStateException("Can't read buffer");
    }
  }

  @Override
  protected LocalTimeValue box(final LocalTime value) {
    return new LocalTimeValue(value);
  }
}
