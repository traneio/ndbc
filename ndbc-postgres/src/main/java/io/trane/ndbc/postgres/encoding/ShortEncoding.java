package io.trane.ndbc.postgres.encoding;

import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.ShortValue;

class ShortEncoding implements Encoding<ShortValue> {
  
  @Override
  public Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.INT2);
  }
  
  @Override
  public Class<ShortValue> valueClass() {
    return ShortValue.class;
  }

  @Override
  public String encodeText(ShortValue value) {
    return Short.toString(value.get());
  }

  @Override
  public ShortValue decodeText(String value) {
    return new ShortValue(Short.valueOf(value));
  }

  @Override
  public void encodeBinary(ShortValue value, BufferWriter b) {
    b.writeShort(value.get());
  }

  @Override
  public ShortValue decodeBinary(BufferReader b) {
    return new ShortValue(b.readShort());
  }
}
