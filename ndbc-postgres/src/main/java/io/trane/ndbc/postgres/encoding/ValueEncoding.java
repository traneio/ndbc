package io.trane.ndbc.postgres.encoding;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.Value;

public class ValueEncoding {

  private static final Set<Encoding<?>> defaultEncodings = Collections.toImmutableSet(
      new BigDecimalEncoding(),
      new BooleanEncoding(),
      new ByteArrayEncoding(),
      new DoubleEncoding(),
      new FloatEncoding(),
      new IntegerEncoding(),
      new LocalDateEncoding(),
      new LocalTimeEncoding(),
      new LongEncoding(),
      new ShortEncoding(),
      new StringEncoding());

  private Map<Class<?>, Encoding<?>> byValueClass;
  private Map<Integer, Encoding<?>> byOid;

  public ValueEncoding(Set<Encoding<?>> customEncodings) {
    byValueClass = new HashMap<>();
    byOid = new HashMap<>();
    registerEncodings(defaultEncodings);
    registerEncodings(customEncodings);
  }

  @SuppressWarnings("unchecked")
  public <T> void encode(Format format, Value<T> value, BufferWriter writer) {
    Encoding<Value<T>> enc;
    if ((enc = (Encoding<Value<T>>) byValueClass.get(value.getClass())) != null)
      enc.encode(format, value, writer);
    else
      throw new UnsupportedOperationException("Can't encode value: " + value);
  }

  public Value<?> decode(int oid, Format format, BufferReader reader) {
    Encoding<?> enc;
    if ((enc = byOid.get(oid)) != null)
      return enc.decode(format, reader);
    else
      throw new RuntimeException("Can't decode value of type " + oid);
  }

  private void registerEncodings(Set<Encoding<?>> encodings) {
    for (Encoding<?> enc : encodings) {
      byValueClass.put(enc.valueClass(), enc);
      for (Integer oid : enc.oids())
        byOid.put(oid, enc);
    }
  }
}
