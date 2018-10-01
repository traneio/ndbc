package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.FieldType;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.FloatValue;

final class FloatEncoding extends Encoding<Float, FloatValue> {

  @Override
  public Key key() {
    return key(FieldType.FLOAT);
  }

  @Override
  public final Class<FloatValue> valueClass() {
    return FloatValue.class;
  }

  @Override
  public final Float decodeText(final String value, Charset charset) {
    return Float.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Float value, final PacketBufferWriter b, Charset charset) {
    b.writeFloat(reverse(value));
  }

  private final float reverse(final Float value) {
    return Float.intBitsToFloat(Integer.reverseBytes(Float.floatToIntBits(value)));
  }

  @Override
  public final Float decodeBinary(final PacketBufferReader b, Key key, Charset charset) {
    return reverse(b.readFloat());
  }

  @Override
  protected FloatValue box(final Float value) {
    return new FloatValue(value);
  }
}
