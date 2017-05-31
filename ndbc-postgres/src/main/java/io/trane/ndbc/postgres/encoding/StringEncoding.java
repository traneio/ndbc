package io.trane.ndbc.postgres.encoding;

import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.StringValue;

class StringEncoding implements Encoding<StringValue> {
  
  @Override
  public Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.TEXT, Oid.NAME, Oid.VARCHAR, Oid.XML, Oid.JSON, Oid.BPCHAR);
  }
  
  @Override
  public Class<StringValue> valueClass() {
    return StringValue.class;
  }

  @Override
  public String encodeText(StringValue value) {
    return value.get();
  }

  @Override
  public StringValue decodeText(String value) {
    return new StringValue(value);
  }

  @Override
  public void encodeBinary(StringValue value, BufferWriter b) {
    b.writeString(value.get());
  }

  @Override
  public StringValue decodeBinary(BufferReader b) {
    return new StringValue(b.readString());
  }
}
