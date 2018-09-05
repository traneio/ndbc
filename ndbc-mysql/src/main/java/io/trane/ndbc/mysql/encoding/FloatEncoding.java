package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.FloatValue;

final class FloatEncoding extends Encoding<Float, FloatValue> {

  public FloatEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public Key key() {
    return key(FieldTypes.FLOAT);
  }

  @Override
  public Class<FloatValue> valueClass() {
    return FloatValue.class;
  }

  @Override
  protected FloatValue box(final Float value) {
    return new FloatValue(value);
  }

  @Override
  protected Float decodeText(final String value) {
    return Float.valueOf(value);
  }

  @Override
  protected void encodeBinary(final Float value, final PacketBufferWriter b) {
    b.writeFloat(value);
  }

  @Override
  protected Float decodeBinary(final PacketBufferReader b, final boolean unsigned) {
    return b.readFloat();
  }
}
