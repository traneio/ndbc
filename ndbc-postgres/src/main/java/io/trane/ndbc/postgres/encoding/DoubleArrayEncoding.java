package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.value.DoubleArrayValue;

final class DoubleArrayEncoding extends ArrayEncoding<Double, DoubleArrayValue> {

  private final DoubleEncoding doubleEncoding;
  private final Double[]       emptyArray = new Double[0];

  public DoubleArrayEncoding(final DoubleEncoding doubleEncoding, Charset charset) {
    super(charset);
    this.doubleEncoding = doubleEncoding;
  }

  @Override
  public final Integer oid() {
    return Oid.FLOAT8_ARRAY;
  }

  @Override
  public final Class<DoubleArrayValue> valueClass() {
    return DoubleArrayValue.class;
  }

  @Override
  protected Double[] newArray(final int length) {
    return new Double[length];
  }

  @Override
  protected Double[] emptyArray() {
    return emptyArray;
  }

  @Override
  protected Encoding<Double, ?> itemEncoding() {
    return doubleEncoding;
  }

  @Override
  protected DoubleArrayValue box(final Double[] value) {
    return new DoubleArrayValue(value);
  }

  @Override
  protected Double[] unbox(final DoubleArrayValue value) {
    return value.getDoubleArray();
  }
}
