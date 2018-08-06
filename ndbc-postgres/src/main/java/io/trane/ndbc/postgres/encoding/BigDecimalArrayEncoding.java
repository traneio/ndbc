package io.trane.ndbc.postgres.encoding;

import java.math.BigDecimal;
import java.nio.charset.Charset;

import io.trane.ndbc.value.BigDecimalArrayValue;

final class BigDecimalArrayEncoding extends ArrayEncoding<BigDecimal, BigDecimalArrayValue> {

  private final BigDecimalEncoding bigDecimalEncoding;
  private final BigDecimal[]       emptyArray = new BigDecimal[0];

  public BigDecimalArrayEncoding(final BigDecimalEncoding bigDecimalEncoding, Charset charset) {
    super(charset);
    this.bigDecimalEncoding = bigDecimalEncoding;
  }

  @Override
  public final Integer oid() {
    return Oid.NUMERIC_ARRAY;
  }

  @Override
  public final Class<BigDecimalArrayValue> valueClass() {
    return BigDecimalArrayValue.class;
  }

  @Override
  protected BigDecimal[] newArray(final int length) {
    return new BigDecimal[length];
  }

  @Override
  protected BigDecimal[] emptyArray() {
    return emptyArray;
  }

  @Override
  protected Encoding<BigDecimal, ?> itemEncoding() {
    return bigDecimalEncoding;
  }

  @Override
  protected BigDecimalArrayValue box(final BigDecimal[] value) {
    return new BigDecimalArrayValue(value);
  }

  @Override
  protected BigDecimal[] unbox(final BigDecimalArrayValue value) {
    return value.getBigDecimalArray();
  }
}
