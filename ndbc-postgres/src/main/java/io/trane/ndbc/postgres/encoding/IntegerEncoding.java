package io.trane.ndbc.postgres.encoding;

import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.IntegerValue;

class IntegerEncoding implements Encoding<IntegerValue> {

  @Override
  public Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.INT4);
  }
  
  @Override
  public Class<IntegerValue> valueClass() {
    return IntegerValue.class;
  }
  
  @Override
  public String encodeText(IntegerValue value) {
    return Integer.toString(value.get());
  }

  @Override
  public IntegerValue decodeText(String value) {
    return new IntegerValue(Integer.valueOf(value));
  }

  @Override
  public void encodeBinary(IntegerValue value, BufferWriter b) {
    b.writeInt(value.get());
  }

  @Override
  public IntegerValue decodeBinary(BufferReader b) {
    return new IntegerValue(b.readInt());
  }
}
