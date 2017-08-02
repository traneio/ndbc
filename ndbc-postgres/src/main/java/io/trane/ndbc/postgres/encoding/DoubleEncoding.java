package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.DoubleValue;

final class DoubleEncoding extends Encoding<Double, DoubleValue> {

  @Override
  public final Integer oid() {
    return Oid.FLOAT8;
  }

  @Override
  public final Class<DoubleValue> valueClass() {
    return DoubleValue.class;
  }

  @Override
  public final String encodeText(final Double value) {
    return Double.toString(value);
  }

  @Override
  public final Double decodeText(final String value) {
    return Double.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Double value, final BufferWriter b) {
    b.writeDouble(value);
  }

  @Override
  public final Double decodeBinary(final BufferReader b) {
    return b.readDouble();
  }

  @Override
  protected DoubleValue box(Double value) {
    return new DoubleValue(value);
  }

  @Override
  protected Double unbox(DoubleValue value) {
    return value.getDouble();
  }
}
