package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;

public interface Encoding<T> {

  default void encode(Format format, T value, BufferWriter writer) {
    if (format == Format.TEXT)
      writer.writeString(encodeText(value));
    else if (format == Format.BINARY)
      encodeBinary(value, writer);
    else
      throw new IllegalStateException("Invalid format: " + format);
  }

  default T decode(Format format, BufferReader reader) {
    if (format == Format.TEXT)
      return decodeText(reader.readString());
    else if (format == Format.BINARY)
      return decodeBinary(reader);
    else
      throw new IllegalStateException("Invalid format: " + format);
  }

  String encodeText(T value);

  T decodeText(String value);

  void encodeBinary(T value, BufferWriter b);

  T decodeBinary(BufferReader b);
}
