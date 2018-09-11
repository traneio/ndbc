package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.FloatValue;

final class FloatEncoding extends Encoding<Float, FloatValue> {

  public FloatEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public final Integer oid() {
    return Oid.FLOAT4;
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
  public final void encodeBinary(final Float value, final BufferWriter b) {
    b.writeFloat(value);
  }

  @Override
  public final Float decodeBinary(final BufferReader b) {
    return b.readFloat();
  }

  @Override
  protected FloatValue box(final Float value) {
    return new FloatValue(value);
  }
}
