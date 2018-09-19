package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.Value;

public abstract class Encoding<T, V extends Value<T>> {

  protected static class Key {
    public final Integer fieldType;
    public final boolean unsigned;

    public Key(Integer fieldType, boolean unsigned) {
      this.fieldType = fieldType;
      this.unsigned = unsigned;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((fieldType == null) ? 0 : fieldType.hashCode());
      result = prime * result + (unsigned ? 1231 : 1237);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Key other = (Key) obj;
      if (fieldType == null) {
        if (other.fieldType != null)
          return false;
      } else if (!fieldType.equals(other.fieldType))
        return false;
      if (unsigned != other.unsigned)
        return false;
      return true;
    }

    @Override
    public String toString() {
      return "Key [fieldType=" + fieldType + ", unsigned=" + unsigned + "]";
    }
  }

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

  public V readBinary(final PacketBufferReader reader, Key key) {
    return box(decodeBinary(reader, key));
  }

  public abstract Key key();

  private static Set<Key> emptyKeys = Collections.unmodifiableSet(new HashSet<>());

  public Set<Key> additionalKeys() {
    return emptyKeys;
  }

  protected static Key unsignedKey(Integer fieldType) {
    return new Key(fieldType, true);
  }

  protected static Key key(Integer fieldType) {
    return new Key(fieldType, false);
  }

  public abstract Class<V> valueClass();

  protected abstract V box(T value);

  protected abstract T decodeText(String value);

  protected abstract void encodeBinary(T value, PacketBufferWriter b);

  protected abstract T decodeBinary(PacketBufferReader b, Key key);
}
