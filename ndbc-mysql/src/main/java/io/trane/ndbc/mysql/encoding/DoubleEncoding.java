package io.trane.ndbc.mysql.encoding;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.FieldType;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.DoubleValue;

final class DoubleEncoding extends Encoding<Double, DoubleValue> {

  @Override
  public Key key() {
    return key(FieldType.DOUBLE);
  }

  @Override
  public final Class<DoubleValue> valueClass() {
    return DoubleValue.class;
  }

  @Override
  public final Double decodeText(final String value, Charset charset) {
    return Double.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Double value, final PacketBufferWriter b, Charset charset) {
    b.writeDouble(reverse(value));
  }

  @Override
  public final Double decodeBinary(final PacketBufferReader b, Key key, Charset charset) {
    return reverse(b.readDouble());
  }

  @Override
  protected DoubleValue box(final Double value) {
    return new DoubleValue(value);
  }

  private static double reverse(double x) {
    ByteBuffer bbuf = ByteBuffer.allocate(8);
    bbuf.order(ByteOrder.BIG_ENDIAN);
    bbuf.putDouble(x);
    bbuf.order(ByteOrder.LITTLE_ENDIAN);
    return bbuf.getDouble(0);
  }
}
