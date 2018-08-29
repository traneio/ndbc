package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Set;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.StringValue;

final class StringEncoding extends Encoding<String, StringValue> {

  public StringEncoding(Charset charset) {
    super(charset);
  }

  @Override
  public Integer fieldType() {
    return FieldTypes.STRING;
  }

  @Override
  public final Set<Integer> additionalOids() {
    return Collections.toImmutableSet(FieldTypes.VAR_STRING, FieldTypes.VARCHAR);
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
  public final String decodeBinary(final PacketBufferReader b) {
    return b.readLengthCodedString(charset);
  }

  @Override
  protected StringValue box(String value) {
    return new StringValue(value);
  }
}
