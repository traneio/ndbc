package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.Value;

public final class EncodingRegistry {

  private final Map<Class<?>, Encoding<?, ?>> byValueClass;
  private final Map<Integer, Encoding<?, ?>>  byOid;

  public EncodingRegistry(final Optional<List<Encoding<?, ?>>> customEncodings, Charset charset) {
    byValueClass = new HashMap<>();
    byOid = new HashMap<>();

    List<Encoding<?, ?>> defaultEncodings = Arrays.asList(new StringEncoding(charset), new LongEncoding(charset));

    registerEncodings(defaultEncodings);
    customEncodings.ifPresent(this::registerEncodings);
  }

  public final <T> void encodeBinary(final Value<T> value, final PacketBufferWriter writer) {
    this.<T>resolve(value).writeBinary(value, writer);
  }

  @SuppressWarnings("unchecked")
  private final <T> Encoding<T, Value<T>> resolve(final Value<?> value) {
    Encoding<T, Value<T>> enc;
    if ((enc = (Encoding<T, Value<T>>) byValueClass.get(value.getClass())) != null)
      return enc;
    else
      throw new UnsupportedOperationException("Can't encode value: " + value);
  }

  public final <T> Value<T> decodeText(final int fieldType, final PacketBufferReader reader) {
    return this.<T>resolve(fieldType).readText(reader);
  }

  public final <T> Value<T> decodeBinary(final int fieldType, final PacketBufferReader reader) {
    return this.<T>resolve(fieldType).readBinary(reader);
  }

  @SuppressWarnings("unchecked")
  private final <T> Encoding<T, Value<T>> resolve(final int fieldType) {
    Encoding<T, Value<T>> enc;
    if ((enc = (Encoding<T, Value<T>>) byOid.get(fieldType)) != null)
      return enc;
    else
      throw new UnsupportedOperationException("Can't decode value of type " + fieldType);
  }

  public final Integer fieldType(final Value<?> value) {
    return byValueClass.get(value.getClass()).fieldType();
  }

  private void registerEncodings(final List<Encoding<?, ?>> encodings) {
    for (final Encoding<?, ?> enc : encodings) {
      byValueClass.put(enc.valueClass(), enc);
      byOid.put(enc.fieldType(), enc);
      for (final Integer oid : enc.additionalOids())
        byOid.put(oid, enc);
    }
  }
}
