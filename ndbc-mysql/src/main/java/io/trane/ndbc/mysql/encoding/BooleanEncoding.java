package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.BooleanValue;

final class BooleanEncoding extends Encoding<Boolean, BooleanValue> {

  public BooleanEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public Key key() {
    return key(FieldTypes.TINY);
  }

  @Override
  public final Class<BooleanValue> valueClass() {
    return BooleanValue.class;
  }

  @Override
  public final Boolean decodeText(final String value) {
    return Boolean.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Boolean value, final PacketBufferWriter b) {
    b.writeByte(value ? (byte) 1 : (byte) 0);
  }

  @Override
  public final Boolean decodeBinary(final PacketBufferReader b, boolean unsigned) {
    return b.readByte() == 1;
  }

  @Override
  protected BooleanValue box(final Boolean value) {
    return new BooleanValue(value);
  }
}
