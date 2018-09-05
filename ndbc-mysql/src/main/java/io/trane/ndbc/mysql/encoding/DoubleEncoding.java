package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.DoubleValue;

final class DoubleEncoding extends Encoding<Double, DoubleValue> {

  public DoubleEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public Key key() {
    return key(FieldTypes.DOUBLE);
  }

  @Override
  public Class<DoubleValue> valueClass() {
    return DoubleValue.class;
  }

  @Override
  protected DoubleValue box(final Double value) {
    return new DoubleValue(value);
  }

  @Override
  protected Double decodeText(final String value) {
    return Double.valueOf(value);
  }

  @Override
  protected void encodeBinary(final Double value, final PacketBufferWriter b) {
    b.writeDouble(value);
  }

  @Override
  protected Double decodeBinary(final PacketBufferReader b, final boolean unsigned) {
    return b.readDouble();
  }
}
