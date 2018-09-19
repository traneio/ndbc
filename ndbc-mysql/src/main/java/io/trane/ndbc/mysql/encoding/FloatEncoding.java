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
  public final Class<FloatValue> valueClass() {
    return FloatValue.class;
  }

  @Override
  public final Float decodeText(final String value) {
    return Float.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Float value, final PacketBufferWriter b) {
    b.writeFloat(value);
  }

  @Override
  public final Float decodeBinary(final PacketBufferReader b, Key key) {
    return b.readFloat();
  }

  @Override
  protected FloatValue box(final Float value) {
    return new FloatValue(value);
  }
}
