package io.trane.ndbc.postgres.encoding;

import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.Value;

public interface Encoding<V extends Value<?>> {
  
  default void encode(Format format, V value, BufferWriter writer) {
    if (format == Format.TEXT)
      writer.writeString(encodeText(value));
    else if (format == Format.BINARY)
      encodeBinary(value, writer);
    else
      throw new IllegalStateException("Invalid format: " + format);
  }

  default V decode(Format format, BufferReader reader) {
    if (format == Format.TEXT)
      return decodeText(reader.readString());
    else if (format == Format.BINARY)
      return decodeBinary(reader);
    else
      throw new IllegalStateException("Invalid format: " + format);
  }
  
  Set<Integer> oids();
  
  Class<V> valueClass();

  String encodeText(V value);

  V decodeText(String value);

  void encodeBinary(V value, BufferWriter b);

  V decodeBinary(BufferReader b);
}
