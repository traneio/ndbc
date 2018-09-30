package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import io.trane.ndbc.mysql.proto.FieldType;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.LocalDateValue;

final class LocalDateEncoding extends Encoding<LocalDate, LocalDateValue> {

  private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
      .appendPattern("yyyy-MM-dd")
      .toFormatter();

  @Override
  public Key key() {
    return key(FieldType.DATE);
  }

  @Override
  public final Class<LocalDateValue> valueClass() {
    return LocalDateValue.class;
  }

  @Override
  public final LocalDate decodeText(final String value, Charset charset) {
    return LocalDate.parse(value, formatter);
  }

  @Override
  public final void encodeBinary(final LocalDate value, final PacketBufferWriter b, Charset charset) {
    b.writeByte((byte) 4);
    b.writeShort(Short.reverseBytes((short) value.getYear()));
    b.writeByte((byte) value.getMonth().getValue());
    b.writeByte((byte) value.getDayOfMonth());
  }

  @Override
  public final LocalDate decodeBinary(final PacketBufferReader b, Key key, Charset charset) {
    assert (b.readByte() == (byte) 4);
    short year = Short.reverseBytes(b.readShort());
    byte month = b.readByte();
    byte day = b.readByte();
    return LocalDate.of(year, month, day);
  }

  @Override
  protected LocalDateValue box(final LocalDate value) {
    return new LocalDateValue(value);
  }
}
