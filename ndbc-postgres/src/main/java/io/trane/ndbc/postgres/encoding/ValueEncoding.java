package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.BooleanValue;
import io.trane.ndbc.value.ByteArrayValue;
import io.trane.ndbc.value.IntegerValue;
import io.trane.ndbc.value.StringValue;
import io.trane.ndbc.value.Value;

public class ValueEncoding {
  
  public void encode(Format format, Value<?> value, BufferWriter writer) {
      throw new UnsupportedOperationException("Can't encode value: " + value);
  }

  public Value<?> decode(int type, Format format, BufferReader reader) {
      return Value.NULL;
  }
}
