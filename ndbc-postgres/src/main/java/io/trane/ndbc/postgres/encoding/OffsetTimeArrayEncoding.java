package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;
import java.time.OffsetTime;

import io.trane.ndbc.value.OffsetTimeArrayValue;

final class OffsetTimeArrayEncoding extends ArrayEncoding<OffsetTime, OffsetTimeArrayValue> {

  private final OffsetTimeEncoding offsetTimeEncoding;
  private final OffsetTime[]       emptyArray = new OffsetTime[0];

  public OffsetTimeArrayEncoding(final OffsetTimeEncoding offsetTimeEncoding, Charset charset) {
    super(charset);
    this.offsetTimeEncoding = offsetTimeEncoding;
  }

  @Override
  public final Integer oid() {
    return Oid.TIMETZ_ARRAY;
  }

  @Override
  public final Class<OffsetTimeArrayValue> valueClass() {
    return OffsetTimeArrayValue.class;
  }

  @Override
  protected OffsetTime[] newArray(final int length) {
    return new OffsetTime[length];
  }

  @Override
  protected OffsetTime[] emptyArray() {
    return emptyArray;
  }

  @Override
  protected Encoding<OffsetTime, ?> itemEncoding() {
    return offsetTimeEncoding;
  }

  @Override
  protected OffsetTimeArrayValue box(final OffsetTime[] value) {
    return new OffsetTimeArrayValue(value);
  }

  @Override
  protected OffsetTime[] unbox(final OffsetTimeArrayValue value) {
    return value.getOffsetTimeArray();
  }
}
