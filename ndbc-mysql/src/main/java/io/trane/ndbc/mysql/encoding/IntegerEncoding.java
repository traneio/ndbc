package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.IntegerValue;

public class IntegerEncoding extends Encoding<Integer, IntegerValue> {

  public IntegerEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public Key key() {
    return key(FieldTypes.INT24);
  }

  @Override
  public Class<IntegerValue> valueClass() {
    return IntegerValue.class;
  }

  @Override
  protected IntegerValue box(final Integer value) {
    return new IntegerValue(value);
  }

  @Override
  protected Integer decodeText(final String value) {
    return Integer.valueOf(value);
  }

  @Override
  protected void encodeBinary(final Integer value, final PacketBufferWriter b) {
    b.writeInt(value);
  }

  @Override
  protected Integer decodeBinary(final PacketBufferReader b, final boolean unsigned) {
    if (unsigned) {
      return b.readUnsignedShort();
    } else {
      return b.readInt();
    }
  }
}
