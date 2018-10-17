package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;
import java.time.LocalTime;

import io.trane.ndbc.postgres.value.LocalTimeArrayValue;

final class LocalTimeArrayEncoding extends ArrayEncoding<LocalTime, LocalTimeArrayValue> {

  private final LocalTimeEncoding localTimeEncoding;
  private final LocalTime[]       emptyArray = new LocalTime[0];

  public LocalTimeArrayEncoding(final LocalTimeEncoding localTimeEncoding, final Charset charset) {
    super(charset);
    this.localTimeEncoding = localTimeEncoding;
  }

  @Override
  public final Integer oid() {
    return Oid.TIME_ARRAY;
  }

  @Override
  public final Class<LocalTimeArrayValue> valueClass() {
    return LocalTimeArrayValue.class;
  }

  @Override
  protected LocalTime[] newArray(final int length) {
    return new LocalTime[length];
  }

  @Override
  protected LocalTime[] emptyArray() {
    return emptyArray;
  }

  @Override
  protected Encoding<LocalTime, ?> itemEncoding() {
    return localTimeEncoding;
  }

  @Override
  protected LocalTimeArrayValue box(final LocalTime[] value) {
    return new LocalTimeArrayValue(value);
  }
}
