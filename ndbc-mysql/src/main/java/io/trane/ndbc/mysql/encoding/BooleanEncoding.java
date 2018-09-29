package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.BooleanValue;

final class BooleanEncoding extends Encoding<Boolean, BooleanValue> {

  private ByteEncoding byteEncoding;

  public BooleanEncoding(ByteEncoding byteEncoding) {
    this.byteEncoding = byteEncoding;
  }

  @Override
  public Key key() {
    return key(FieldTypes.TINY);
  }

  @Override
  public final Class<BooleanValue> valueClass() {
    return BooleanValue.class;
  }

  @Override
  public final Boolean decodeText(final String value, Charset charset) {
    return !value.equals("0") || Boolean.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Boolean value, final PacketBufferWriter b, Charset charset) {
    byteEncoding.encodeBinary(value ? (byte) 1 : (byte) 0, b, charset);
  }

  @Override
  public final Boolean decodeBinary(final PacketBufferReader b, Key key, Charset charset) {
    return byteEncoding.decodeBinary(b, key, charset) != 0;
  }

  @Override
  protected BooleanValue box(final Boolean value) {
    return new BooleanValue(value);
  }
}
