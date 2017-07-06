package io.trane.ndbc.postgres.encoding;

import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.Value;

public abstract class Encoding<V extends Value<?>> {

  public void encode(final Format format, final V value, final BufferWriter writer) {
    if (format == Format.TEXT)
      writer.writeString(encodeText(value));
    else
      encodeBinary(value, writer);
  }

  public V decode(final Format format, final BufferReader reader) {
    if (format == Format.TEXT)
      return decodeText(reader.readString());
    else
      return decodeBinary(reader);
  }

  public abstract Set<Integer> oids();

  public abstract Class<V> valueClass();

  protected abstract String encodeText(V value);

  protected abstract V decodeText(String value);

  protected abstract void encodeBinary(V value, BufferWriter b);

  protected abstract V decodeBinary(BufferReader b);
}
