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
  public final Class<DoubleValue> valueClass() {
    return DoubleValue.class;
  }

  @Override
  public final Double decodeText(final String value) {
    return Double.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Double value, final PacketBufferWriter b) {
    b.writeDouble(value);
  }

  @Override
  public final Double decodeBinary(final PacketBufferReader b, Key key) {
    return b.readDouble();
  }

  @Override
  protected DoubleValue box(final Double value) {
    return new DoubleValue(value);
  }
}
