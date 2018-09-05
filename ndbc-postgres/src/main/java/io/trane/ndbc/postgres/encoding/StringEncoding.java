package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;
import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.StringValue;

final class StringEncoding extends Encoding<String, StringValue> {

  public StringEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public Integer oid() {
    return Oid.VARCHAR;
  }

  @Override
  public final Set<Integer> additionalOids() {
    return Collections.toImmutableSet(Oid.NAME, Oid.TEXT, Oid.BPCHAR);
  }

  @Override
  public final Class<StringValue> valueClass() {
    return StringValue.class;
  }

  @Override
  public final String encodeText(final String value) {
    return value;
  }

  @Override
  public final String decodeText(final String value) {
    return value;
  }

  @Override
  public final void encodeBinary(final String value, final BufferWriter b) {
    b.writeString(value);
  }

  @Override
  public final String decodeBinary(final BufferReader b) {
    return b.readString(charset);
  }

  @Override
  protected StringValue box(final String value) {
    return new StringValue(value);
  }
}
