package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.LongValue;

final class LongEncoding extends Encoding<Long, LongValue> {

  public LongEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public final Integer oid() {
    return Oid.INT8;
  }

  @Override
  public final Class<LongValue> valueClass() {
    return LongValue.class;
  }

  @Override
  public final String encodeText(final Long value) {
    return Long.toString(value);
  }

  @Override
  public final Long decodeText(final String value) {
    return Long.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Long value, final BufferWriter b) {
    b.writeLong(value);
  }

  @Override
  public final Long decodeBinary(final BufferReader b) {
    return b.readLong();
  }

  @Override
  protected LongValue box(final Long value) {
    return new LongValue(value);
  }
}
