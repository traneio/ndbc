package io.trane.ndbc.postgres.encoding;

import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.FloatValue;

class FloatEncoding implements Encoding<FloatValue> {
  
  @Override
  public Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.FLOAT4);
  }
  
  @Override
  public Class<FloatValue> valueClass() {
    return FloatValue.class;
  }

  @Override
  public String encodeText(FloatValue value) {
    return Float.toString(value.get());
  }

  @Override
  public FloatValue decodeText(String value) {
    return new FloatValue(Float.valueOf(value));
  }

  @Override
  public void encodeBinary(FloatValue value, BufferWriter b) {
    b.writeFloat(value.get());
  }

  @Override
  public FloatValue decodeBinary(BufferReader b) {
    return new FloatValue(b.readFloat());
  }
}
