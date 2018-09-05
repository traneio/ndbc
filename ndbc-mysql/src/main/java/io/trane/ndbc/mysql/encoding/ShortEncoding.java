package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.ShortValue;

public class ShortEncoding extends Encoding<Short, ShortValue> {

  public ShortEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public Key key() {
    return key(FieldTypes.SHORT);
  }

  @Override
  public Class<ShortValue> valueClass() {
    return ShortValue.class;
  }

  @Override
  protected ShortValue box(final Short value) {
    return new ShortValue(value);
  }

  @Override
  protected Short decodeText(final String value) {
    return Short.valueOf(value);
  }

  @Override
  protected void encodeBinary(final Short value, final PacketBufferWriter b) {
    b.writeShort(value);
  }

  @Override
  protected Short decodeBinary(final PacketBufferReader b, final boolean unsigned) {
    if (unsigned) {
      return b.readUnsignedByte();
    } else {
      return b.readShort();
    }
  }
}
