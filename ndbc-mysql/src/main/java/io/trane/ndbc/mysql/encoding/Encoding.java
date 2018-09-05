package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.Value;

public abstract class Encoding<T, V extends Value<T>> {

  protected final Charset charset;

  public Encoding(final Charset charset) {
    this.charset = charset;
  }

  public void writeBinary(final V value, final PacketBufferWriter writer) {
    encodeBinary(value.get(), writer);
  }

  public V readText(final PacketBufferReader reader) {
    return box(decodeText(reader.readLengthCodedString(charset)));
  }

  public V readBinary(final PacketBufferReader reader) {
    return box(decodeBinary(reader));
  }

  public abstract Integer fieldType();

  private static Set<Integer> emptyOids = Collections.unmodifiableSet(new HashSet<>());

  public Set<Integer> additionalOids() {
    return emptyOids;
  }

  public abstract Class<V> valueClass();

  protected abstract V box(T value);

  protected abstract T decodeText(String value);

  protected abstract void encodeBinary(T value, PacketBufferWriter b);

  protected abstract T decodeBinary(PacketBufferReader b);
}
