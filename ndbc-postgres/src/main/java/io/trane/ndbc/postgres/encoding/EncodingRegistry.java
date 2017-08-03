package io.trane.ndbc.postgres.encoding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.Value;

public final class EncodingRegistry {

  private static final List<Encoding<?, ?>> defaultEncodings = Arrays.asList(new BigDecimalEncoding(),
      new BooleanEncoding(), new ByteArrayEncoding(), new DoubleEncoding(), new FloatEncoding(), new IntegerEncoding(),
      new LocalDateEncoding(), new LocalDateTimeEncoding(), new LocalTimeEncoding(), new LongEncoding(),
      new UUIDEncoding(), new OffsetTimeEncoding(), new ByteEncoding(), new ShortEncoding(), new StringEncoding(),

      new BigDecimalArrayEncoding(new BigDecimalEncoding()), new BooleanArrayEncoding(new BooleanEncoding()),
      new ByteArrayArrayEncoding(new ByteArrayEncoding()), new DoubleArrayEncoding(new DoubleEncoding()),
      new FloatArrayEncoding(new FloatEncoding()), new IntegerArrayEncoding(new IntegerEncoding()),
      new LocalDateArrayEncoding(new LocalDateEncoding()), new LocalDateTimeArrayEncoding(new LocalDateTimeEncoding()),
      new LocalTimeArrayEncoding(new LocalTimeEncoding()), new LongArrayEncoding(new LongEncoding()),
      new UUIDArrayEncoding(new UUIDEncoding()), new OffsetTimeArrayEncoding(new OffsetTimeEncoding()),
      new ShortArrayEncoding(new ShortEncoding()), new StringArrayEncoding(new StringEncoding()));

  private final Map<Class<?>, Encoding<?, ?>> byValueClass;
  private final Map<Integer, Encoding<?, ?>> byOid;

  public EncodingRegistry(final Optional<List<Encoding<?, ?>>> customEncodings) {
    byValueClass = new HashMap<>();
    byOid = new HashMap<>();
    registerEncodings(defaultEncodings);
    customEncodings.ifPresent(this::registerEncodings);
  }

  @SuppressWarnings("unchecked")
  public final <T> void encode(final Format format, final Value<T> value, final BufferWriter writer) {
    Encoding<T, Value<T>> enc;
    if ((enc = (Encoding<T, Value<T>>) byValueClass.get(value.getClass())) != null)
      enc.encode(format, value, writer);
    else
      throw new UnsupportedOperationException("Can't encode value: " + value);
  }

  public final Value<?> decode(final int oid, final Format format, final BufferReader reader) {
    Encoding<?, ?> enc;
    if ((enc = byOid.get(oid)) != null)
      return enc.decode(format, reader);
    else
      throw new UnsupportedOperationException("Can't decode value of type " + oid);
  }

  public final Integer oid(Value<?> value) {
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
