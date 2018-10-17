package io.trane.ndbc.mysql.encoding;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.trane.ndbc.NdbcException;
import io.trane.ndbc.mysql.encoding.Encoding.Key;
import io.trane.ndbc.mysql.proto.FieldType;
import io.trane.ndbc.mysql.proto.Message.Field;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.value.Value;

public final class EncodingRegistry {

  private final Map<Class<?>, Encoding<?, ?>> byValueClass;
  private final Map<Key, Encoding<?, ?>>      byKey;
  private final Charset                       charset;

  public EncodingRegistry(final Optional<List<Encoding<?, ?>>> customEncodings, final Charset charset) {
    this.charset = charset;
    byValueClass = new HashMap<>();
    byKey = new HashMap<>();

    final ByteEncoding byteEncoding = new ByteEncoding();
    final List<Encoding<?, ?>> defaultEncodings = Arrays.asList(
        new BooleanEncoding(byteEncoding),
        new ByteArrayEncoding(),
        byteEncoding,
        new DoubleEncoding(),
        new FloatEncoding(),
        new ShortEncoding(),
        new IntegerEncoding(),
        new BigDecimalEncoding(),
        new StringEncoding(),
        new LongEncoding(),
        new LocalDateEncoding(),
        new LocalDateTimeEncoding(),
        new LocalTimeEncoding());

    registerEncodings(defaultEncodings);
    customEncodings.ifPresent(this::registerEncodings);
  }

  public final <T> void encodeBinary(final Value<T> value, final PacketBufferWriter writer, final Charset charset) {
    this.<T>resolve(value).writeBinary(value, writer, charset);
  }

  @SuppressWarnings("unchecked")
  private final <T> Encoding<T, Value<T>> resolve(final Value<?> value) {
    Encoding<T, Value<T>> enc;
    if ((enc = (Encoding<T, Value<T>>) byValueClass.get(value.getClass())) != null)
      return enc;
    else
      throw new NdbcException("Can't encode value: " + value);
  }

  public final <T> Value<T> decodeText(final Field field, final PacketBufferReader reader) {
    final Key key = keyFor(field);
    return this.<T>resolve(key).readText(reader, key, charset);
  }

  public final <T> Value<T> decodeBinary(final Field field, final PacketBufferReader reader) {
    final Key key = keyFor(field);
    return this.<T>resolve(key).readBinary(reader, key, charset);
  }

  private final Key keyFor(final Field field) {
    return new Key(field.fieldType, isUnsigned(field));
  }

  @SuppressWarnings("unchecked")
  private final <T> Encoding<T, Value<T>> resolve(final Key key) {
    Encoding<T, Value<T>> enc;
    if ((enc = (Encoding<T, Value<T>>) byKey.get(key)) != null)
      return enc;
    else
      throw new NdbcException("Can't decode value " + key);
  }

  private boolean isUnsigned(final Field field) {
    return (field.flags & FieldAttributes.UnsignedBitMask) > 0;
  }

  public final FieldType fieldType(final Value<?> value) {
    final Encoding<?, ?> encoding = byValueClass.get(value.getClass());
    if (encoding == null)
      throw new NdbcException("Can't encode " + value);
    return encoding.key().fieldType;
  }

  private void registerEncodings(final List<Encoding<?, ?>> encodings) {
    for (final Encoding<?, ?> enc : encodings) {
      byValueClass.put(enc.valueClass(), enc);
      byKey.put(enc.key(), enc);
      for (final Key key : enc.additionalKeys())
        byKey.put(key, enc);
    }
  }
}
