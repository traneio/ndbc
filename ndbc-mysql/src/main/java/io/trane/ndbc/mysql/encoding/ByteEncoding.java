package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.FieldType;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.ByteValue;

final class ByteEncoding extends Encoding<Byte, ByteValue> {

  @Override
  public Key key() {
    return key(FieldType.TINY);
  }

  @Override
  public final Class<ByteValue> valueClass() {
    return ByteValue.class;
  }

  @Override
  public final Byte decodeText(final String value, final Charset charset) {
    return Byte.valueOf(value);
  }

  @Override
  public final void encodeBinary(final Byte value, final PacketBufferWriter b, final Charset charset) {
    b.writeByte(value);
  }

  @Override
  public final Byte decodeBinary(final PacketBufferReader b, final Key key, final Charset charset) {
    return b.readByte();
  }

  @Override
  protected ByteValue box(final Byte value) {
    return new ByteValue(value);
  }
}
