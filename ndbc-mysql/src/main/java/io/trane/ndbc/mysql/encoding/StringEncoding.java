package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Set;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.StringValue;

final class StringEncoding extends Encoding<String, StringValue> {

  public StringEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public Key key() {
    return key(FieldTypes.VARCHAR);
  }

  @Override
  public Set<Key> additionalKeys() {
    return Collections.toImmutableSet(key(FieldTypes.STRING), key(FieldTypes.VAR_STRING), key(FieldTypes.TINY_BLOB),
        key(FieldTypes.BLOB), key(FieldTypes.MEDIUM_BLOB));
  }

  @Override
  public final Class<StringValue> valueClass() {
    return StringValue.class;
  }

  @Override
  public final String decodeText(final String value) {
    return value;
  }

  @Override
  public final void encodeBinary(final String value, final PacketBufferWriter b) {
    b.writeLengthCodedString(charset, value);
  }

  @Override
  public final String decodeBinary(final PacketBufferReader b, Key key) {
    return b.readLengthCodedString(charset);
  }

  @Override
  protected StringValue box(final String value) {
    return new StringValue(value);
  }
}
