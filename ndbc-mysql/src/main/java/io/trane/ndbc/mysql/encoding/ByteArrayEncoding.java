package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Set;

import io.trane.ndbc.mysql.proto.FieldType;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.ByteArrayValue;

final class ByteArrayEncoding extends Encoding<byte[], ByteArrayValue> {

  @Override
  public Key key() {
    return key(FieldType.BLOB);
  }

  @Override
  public Set<Key> additionalKeys() {
    return Collections.toImmutableSet(key(FieldType.TINY_BLOB), key(FieldType.MEDIUM_BLOB));
  }

  @Override
  public final Class<ByteArrayValue> valueClass() {
    return ByteArrayValue.class;
  }

  @Override
  public ByteArrayValue readText(final PacketBufferReader reader, final Key key, final Charset charset) {
    return box(decodeBinary(reader, key, charset));
  }

  @Override
  public final byte[] decodeText(final String value, final Charset charset) {
    return value.getBytes(charset);
  }

  @Override
  public final void encodeBinary(final byte[] value, final PacketBufferWriter b, final Charset charset) {
    b.writeLengthCodedBytes(value);
  }

  @Override
  public final byte[] decodeBinary(final PacketBufferReader b, final Key key, final Charset charset) {
    return b.readLengthCodedBytes();
  }

  @Override
  protected ByteArrayValue box(final byte[] value) {
    return new ByteArrayValue(value);
  }
}
