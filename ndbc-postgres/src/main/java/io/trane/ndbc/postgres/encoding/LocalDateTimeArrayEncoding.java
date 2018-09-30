package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

import io.trane.ndbc.postgres.value.LocalDateTimeArrayValue;

final class LocalDateTimeArrayEncoding extends ArrayEncoding<LocalDateTime, LocalDateTimeArrayValue> {

  private final LocalDateTimeEncoding localDateTimeEncoding;
  private final LocalDateTime[]       emptyArray = new LocalDateTime[0];

  public LocalDateTimeArrayEncoding(final LocalDateTimeEncoding localDateTimeEncoding, final Charset charset) {
    super(charset);
    this.localDateTimeEncoding = localDateTimeEncoding;
  }

  @Override
  public final Integer oid() {
    return Oid.TIMESTAMP_ARRAY;
  }

  @Override
  public final Class<LocalDateTimeArrayValue> valueClass() {
    return LocalDateTimeArrayValue.class;
  }

  @Override
  protected LocalDateTime[] newArray(final int length) {
    return new LocalDateTime[length];
  }

  @Override
  protected LocalDateTime[] emptyArray() {
    return emptyArray;
  }

  @Override
  protected Encoding<LocalDateTime, ?> itemEncoding() {
    return localDateTimeEncoding;
  }

  @Override
  protected LocalDateTimeArrayValue box(final LocalDateTime[] value) {
    return new LocalDateTimeArrayValue(value);
  }
}
