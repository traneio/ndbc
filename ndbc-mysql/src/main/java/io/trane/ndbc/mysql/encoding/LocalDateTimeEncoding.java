package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Set;

import io.trane.ndbc.mysql.proto.FieldType;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.LocalDateTimeValue;

final class LocalDateTimeEncoding extends Encoding<LocalDateTime, LocalDateTimeValue> {

  private static final DateTimeFormatter withNanos = new DateTimeFormatterBuilder()
      .appendPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
      .toFormatter();

  private static final DateTimeFormatter withoutNanos = new DateTimeFormatterBuilder()
      .appendPattern("yyyy-MM-dd HH:mm:ss")
      .toFormatter();

  @Override
  public Key key() {
    return key(FieldType.DATETIME);
  }

  @Override
  public Set<Key> additionalKeys() {
    return Collections.toImmutableSet(key(FieldType.TIMESTAMP), unsignedKey(FieldType.TIMESTAMP));
  }

  @Override
  public final Class<LocalDateTimeValue> valueClass() {
    return LocalDateTimeValue.class;
  }

  @Override
  public final LocalDateTime decodeText(final String value, Charset charset) {
    try {
      return LocalDateTime.parse(value, withNanos);
    } catch (DateTimeParseException ex) {
      return LocalDateTime.parse(value, withoutNanos);
    }
  }

  @Override
  public final void encodeBinary(final LocalDateTime value, final PacketBufferWriter b, Charset charset) {
    if (value.getNano() != 0)
      b.writeByte((byte) 11);
    else
      b.writeByte((byte) 7);

    b.writeShort(Short.reverseBytes((short) value.getYear()));
    b.writeByte((byte) value.getMonth().getValue());
    b.writeByte((byte) value.getDayOfMonth());
    b.writeByte((byte) value.getHour());
    b.writeByte((byte) value.getMinute());
    b.writeByte((byte) value.getSecond());

    if (value.getNano() != 0)
      b.writeInt(Integer.reverseBytes(value.getNano() / 1000));
  }

  @Override
  public final LocalDateTime decodeBinary(final PacketBufferReader b, Key key, Charset charset) {
    byte length = b.readByte();
    short year = Short.reverseBytes(b.readShort());
    byte month = b.readByte();
    byte day = b.readByte();
    byte hour = b.readByte();
    byte minute = b.readByte();
    byte second = b.readByte();
    int nanos = 0;
    if (length == 11)
      nanos = Integer.reverseBytes(b.readInt()) * 1000;
    return LocalDateTime.of(year, month, day, hour, minute, second, nanos);
  }

  @Override
  protected LocalDateTimeValue box(final LocalDateTime value) {
    return new LocalDateTimeValue(value);
  }
}
