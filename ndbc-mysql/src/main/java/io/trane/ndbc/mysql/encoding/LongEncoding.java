package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.LongValue;

final class LongEncoding extends Encoding<Long, LongValue> {

  public LongEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public Integer fieldType() {
    return FieldTypes.LONGLONG;
  }

  @Override
  public final Class<LongValue> valueClass() {
    return LongValue.class;
  }

  @Override
  public final Long decodeText(final String value) {
    return Long.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Long value, final PacketBufferWriter b) {
    b.writeLong(value);
  }

  @Override
  public final Long decodeBinary(final PacketBufferReader b) {
    return b.readLong();
  }

  @Override
  protected LongValue box(final Long value) {
    return new LongValue(value);
  }
}
