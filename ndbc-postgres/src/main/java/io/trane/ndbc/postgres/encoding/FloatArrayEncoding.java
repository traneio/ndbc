package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.postgres.value.FloatArrayValue;

final class FloatArrayEncoding extends ArrayEncoding<Float, FloatArrayValue> {

  private final FloatEncoding floatEncoding;
  private final Float[]       emptyArray = new Float[0];

  public FloatArrayEncoding(final FloatEncoding floatEncoding, final Charset charset) {
    super(charset);
    this.floatEncoding = floatEncoding;
  }

  @Override
  public final Integer oid() {
    return Oid.FLOAT4_ARRAY;
  }

  @Override
  public final Class<FloatArrayValue> valueClass() {
    return FloatArrayValue.class;
  }

  @Override
  protected Float[] newArray(final int length) {
    return new Float[length];
  }

  @Override
  protected Float[] emptyArray() {
    return emptyArray;
  }

  @Override
  protected Encoding<Float, ?> itemEncoding() {
    return floatEncoding;
  }

  @Override
  protected FloatArrayValue box(final Float[] value) {
    return new FloatArrayValue(value);
  }
}
