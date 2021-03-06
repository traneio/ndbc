package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.IntegerValue;

final class IntegerEncoding extends Encoding<Integer, IntegerValue> {

  public IntegerEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public final Integer oid() {
    return Oid.INT4;
  }

  @Override
  public final Class<IntegerValue> valueClass() {
    return IntegerValue.class;
  }

  @Override
  public final Integer decodeText(final String value) {
    return Integer.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Integer value, final BufferWriter b) {
    b.writeInt(value);
  }

  @Override
  public final Integer decodeBinary(final BufferReader b) {
    return b.readInt();
  }

  @Override
  protected IntegerValue box(final Integer value) {
    return new IntegerValue(value);
  }
}
