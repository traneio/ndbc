package io.trane.ndbc.postgres.encoding;

import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.StringValue;

final class StringEncoding extends Encoding<StringValue> {

  @Override
  public final Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.TEXT, Oid.NAME, Oid.VARCHAR, Oid.XML, Oid.JSON,
        Oid.BPCHAR);
  }

  @Override
  public final Class<StringValue> valueClass() {
    return StringValue.class;
  }

  @Override
  public final String encodeText(final StringValue value) {
    return value.getString();
  }

  @Override
  public final StringValue decodeText(final String value) {
    return new StringValue(value);
  }

  @Override
  public final void encodeBinary(final StringValue value, final BufferWriter b) {
    b.writeString(value.getString());
  }

  @Override
  public final StringValue decodeBinary(final BufferReader b) {
    return new StringValue(b.readString());
  }
}
