package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;
import java.util.Set;

import io.trane.ndbc.postgres.value.StringArrayValue;
import io.trane.ndbc.util.Collections;

final class StringArrayEncoding extends ArrayEncoding<String, StringArrayValue> {

  private final StringEncoding stringEncoding;
  private final String[]       emptyArray = new String[0];

  public StringArrayEncoding(final StringEncoding stringEncoding, final Charset charset) {
    super(charset);
    this.stringEncoding = stringEncoding;
  }

  @Override
  public final Integer oid() {
    return Oid.VARCHAR_ARRAY;
  }

  @Override
  public final Set<Integer> additionalOids() {
    return Collections.toImmutableSet(Oid.NAME_ARRAY, Oid.TEXT_ARRAY, Oid.BPCHAR_ARRAY);
  }

  @Override
  public final Class<StringArrayValue> valueClass() {
    return StringArrayValue.class;
  }

  @Override
  protected String[] newArray(final int length) {
    return new String[length];
  }

  @Override
  protected String[] emptyArray() {
    return emptyArray;
  }

  @Override
  protected Encoding<String, ?> itemEncoding() {
    return stringEncoding;
  }

  @Override
  protected StringArrayValue box(final String[] value) {
    return new StringArrayValue(value);
  }
}
