package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.trane.ndbc.NdbcException;
import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.Value;

public final class EncodingRegistry {

  private final Map<Class<?>, Encoding<?, ?>> byValueClass;
  private final Map<Integer, Encoding<?, ?>>  byOid;

  public EncodingRegistry(final Optional<List<Encoding<?, ?>>> customEncodings, Charset charset) {
    byValueClass = new HashMap<>();
    byOid = new HashMap<>();

    List<Encoding<?, ?>> defaultEncodings = Arrays.asList(new BigDecimalEncoding(charset),
        new BooleanEncoding(charset), new ByteArrayEncoding(charset), new DoubleEncoding(charset),
        new FloatEncoding(charset), new IntegerEncoding(charset), new LocalDateEncoding(charset),
        new LocalDateTimeEncoding(charset), new LocalTimeEncoding(charset), new LongEncoding(charset),
        new UUIDEncoding(charset), new OffsetTimeEncoding(charset), new ByteEncoding(charset),
        new ShortEncoding(charset), new StringEncoding(charset),
        new BigDecimalArrayEncoding(new BigDecimalEncoding(charset), charset),
        new BooleanArrayEncoding(new BooleanEncoding(charset), charset),
        new ByteArrayArrayEncoding(new ByteArrayEncoding(charset), charset),
        new DoubleArrayEncoding(new DoubleEncoding(charset), charset),
        new FloatArrayEncoding(new FloatEncoding(charset), charset),
        new IntegerArrayEncoding(new IntegerEncoding(charset), charset),
        new LocalDateArrayEncoding(new LocalDateEncoding(charset), charset),
        new LocalDateTimeArrayEncoding(new LocalDateTimeEncoding(charset), charset),
        new LocalTimeArrayEncoding(new LocalTimeEncoding(charset), charset),
        new LongArrayEncoding(new LongEncoding(charset), charset),
        new UUIDArrayEncoding(new UUIDEncoding(charset), charset),
        new OffsetTimeArrayEncoding(new OffsetTimeEncoding(charset), charset),
        new ShortArrayEncoding(new ShortEncoding(charset), charset),
        new StringArrayEncoding(new StringEncoding(charset), charset));

    registerEncodings(defaultEncodings);
    customEncodings.ifPresent(this::registerEncodings);
  }

  @SuppressWarnings("unchecked")
  public final <T> void encode(final Format format, final Value<T> value, final BufferWriter writer) {
    Encoding<T, Value<T>> enc;
    if ((enc = (Encoding<T, Value<T>>) byValueClass.get(value.getClass())) != null)
      enc.encode(format, value, writer);
    else
      throw new NdbcException("Can't encode value: " + value);
  }

  public final Value<?> decode(final int oid, final Format format, final BufferReader reader) {
    Encoding<?, ?> enc;
    if ((enc = byOid.get(oid)) != null)
      return enc.decode(format, reader);
    else
      throw new NdbcException("Can't decode value of type " + oid);
  }

  public final Integer oid(final Value<?> value) {
    if (value.isNull())
      return Oid.UNSPECIFIED;
    else
      return byValueClass.get(value.getClass()).oid();
  }

  private void registerEncodings(final List<Encoding<?, ?>> encodings) {
    for (final Encoding<?, ?> enc : encodings) {
      byValueClass.put(enc.valueClass(), enc);
      byOid.put(enc.oid(), enc);
      for (final Integer oid : enc.additionalOids())
        byOid.put(oid, enc);
    }
  }
}
