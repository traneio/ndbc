package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Set;

import io.trane.ndbc.mysql.proto.FieldType;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.StringValue;

final class StringEncoding extends Encoding<String, StringValue> {

  @Override
  public Key key() {
    return key(FieldType.VARCHAR);
  }

  @Override
  public Set<Key> additionalKeys() {
    return Collections.toImmutableSet(key(FieldType.STRING), key(FieldType.VAR_STRING));
  }

  @Override
  public final Class<StringValue> valueClass() {
    return StringValue.class;
  }

  @Override
  public final String decodeText(final String value, Charset charset) {
    return value;
  }

  @Override
  public final void encodeBinary(final String value, final PacketBufferWriter b, Charset charset) {
    b.writeLengthCodedString(charset, value);
  }

  @Override
  public final String decodeBinary(final PacketBufferReader b, Key key, Charset charset) {
    return b.readLengthCodedString(charset);
  }

  @Override
  protected StringValue box(final String value) {
    return new StringValue(value);
  }
}
