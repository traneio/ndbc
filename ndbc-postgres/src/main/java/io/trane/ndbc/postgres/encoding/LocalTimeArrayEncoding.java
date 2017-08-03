package io.trane.ndbc.postgres.encoding;

import java.time.LocalTime;

import io.trane.ndbc.value.LocalTimeArrayValue;

final class LocalTimeArrayEncoding extends ArrayEncoding<LocalTime, LocalTimeArrayValue> {

  private final LocalTimeEncoding localTimeEncoding;
  private final LocalTime[]       emptyArray = new LocalTime[0];

  public LocalTimeArrayEncoding(LocalTimeEncoding localTimeEncoding) {
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
  protected LocalTime[] newArray(int length) {
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
  protected LocalTimeArrayValue box(LocalTime[] value) {
    return new LocalTimeArrayValue(value);
  }

  @Override
  protected LocalTime[] unbox(LocalTimeArrayValue value) {
    return value.getLocalTimeArray();
  }
}
