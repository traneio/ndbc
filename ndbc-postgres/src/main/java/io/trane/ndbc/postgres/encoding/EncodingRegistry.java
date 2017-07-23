package io.trane.ndbc.postgres.encoding;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.Value;

public final class EncodingRegistry {

  private static final Set<Encoding<?>>    defaultEncodings = Collections.toImmutableSet(
      new BigDecimalEncoding(), new BooleanEncoding(), new ByteArrayEncoding(),
      new DoubleEncoding(), new FloatEncoding(), new IntegerEncoding(), new LocalDateEncoding(),
      new LocalDateTimeEncoding(), new LocalTimeEncoding(), new LongEncoding(),
      new OffsetTimeEncoding(), new ShortEncoding(), new StringEncoding());

  private final Map<Class<?>, Encoding<?>> byValueClass;
  private final Map<Integer, Encoding<?>>  byOid;

  public EncodingRegistry(final Optional<Set<Encoding<?>>> customEncodings) {
    byValueClass = new HashMap<>();
    byOid = new HashMap<>();
    registerEncodings(defaultEncodings);
    customEncodings.ifPresent(this::registerEncodings);
  }

  @SuppressWarnings("unchecked")
  public final <T> void encode(final Format format, final Value<T> value,
      final BufferWriter writer) {
    Encoding<Value<T>> enc;
    if ((enc = (Encoding<Value<T>>) byValueClass.get(value.getClass())) != null)
      enc.encode(format, value, writer);
    else
      throw new UnsupportedOperationException("Can't encode value: " + value);
  }

  public final Value<?> decode(final int oid, final Format format, final BufferReader reader) {
    Encoding<?> enc;
    if ((enc = byOid.get(oid)) != null)
      return enc.decode(format, reader);
    else
      throw new UnsupportedOperationException("Can't decode value of type " + oid);
  }
  
  public final Integer oid(Value<?> value) {
    return byValueClass.get(value.getClass()).oid();
  }

  private void registerEncodings(final Set<Encoding<?>> encodings) {
    for (final Encoding<?> enc : encodings) {
      byValueClass.put(enc.valueClass(), enc);
      byOid.put(enc.oid(), enc);
      for (final Integer oid : enc.additionalOids())
        byOid.put(oid, enc);
    }
  }
}
