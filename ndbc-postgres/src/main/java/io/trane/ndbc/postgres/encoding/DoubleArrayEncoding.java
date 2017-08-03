package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.value.DoubleArrayValue;

final class DoubleArrayEncoding extends ArrayEncoding<Double, DoubleArrayValue> {

  private final DoubleEncoding doubleEncoding;
  private final Double[]       emptyArray = new Double[0];

  public DoubleArrayEncoding(DoubleEncoding doubleEncoding) {
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
  protected Double[] newArray(int length) {
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
  protected DoubleArrayValue box(Double[] value) {
    return new DoubleArrayValue(value);
  }

  @Override
  protected Double[] unbox(DoubleArrayValue value) {
    return value.getDoubleArray();
  }
}
