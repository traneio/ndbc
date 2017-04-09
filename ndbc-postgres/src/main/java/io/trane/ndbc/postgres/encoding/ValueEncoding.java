package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.IntegerValue;
import io.trane.ndbc.value.StringValue;
import io.trane.ndbc.value.Value;

public class ValueEncoding {

  private static final Encoding<String> stringEncoding = new StringEncoding();
  private static final Encoding<Integer> integerEncoding = new IntegerEncoding();

  public void encode(Format format, Value<?> value, BufferWriter writer) {
    if (value instanceof StringValue)
      stringEncoding.encode(format, ((StringValue) value).get(), writer);
    else if (value instanceof IntegerValue)
      integerEncoding.encode(format, ((IntegerValue) value).get(), writer);
    else
      throw new UnsupportedOperationException("Can't encode value: " + value);
  }

  public Value<?> decode(int type, Format format, BufferReader reader) {
    if (reader == null)
      return Value.NULL;
    else 
      switch (type) {
      case Oid.TEXT:
      case Oid.VARCHAR:
      case Oid.BPCHAR:
        return new StringValue(stringEncoding.decode(format, reader));
      case Oid.INT2:
        return new IntegerValue(integerEncoding.decode(format, reader));
      default:
        throw new IllegalStateException("Can't decode type: " + type);
      }
  }
}
