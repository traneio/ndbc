package io.trane.ndbc.postgres.encoding;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.BigDecimalValue;

/**
 * Java version of finagle-postgres' Numerics (https://github.com/finagle/finagle-postgres/blob/69ab3983d6acc6aa4a8e029c96cc1cb224d6c40d/src/main/scala/com/twitter/finagle/postgres/values/Numerics.scala)
 */
class BigDecimalEncoding implements Encoding<BigDecimalValue> {

  private static final BigInteger BI_BASE = BigInteger.valueOf(10000);
  private static final short[] EMPTY_SHORT_ARRAY = new short[0];
  private static final short NUMERIC_POS = 0x0000;
  private static final short NUMERIC_NEG = 0x4000;
  private static final int NUMERIC_NAN = 0xC000;
  private static final int NUMERIC_NULL = 0xF000;
  private static final int EXPONENT = 4;
  private static final BigDecimalValue ZERO = new BigDecimalValue(new BigDecimal(0));

  @Override
  public Set<Integer> oids() {
    return Collections.toImmutableSet(Oid.NUMERIC);
  }
  
  @Override
  public Class<BigDecimalValue> valueClass() {
    return BigDecimalValue.class;
  }
  
  @Override
  public String encodeText(BigDecimalValue value) {
    return value.get().toPlainString();
  }

  @Override
  public BigDecimalValue decodeText(String value) {
    return new BigDecimalValue(new BigDecimal(value));
  }

  @Override
  public void encodeBinary(BigDecimalValue value, BufferWriter b) {
    BigDecimal minimized = value.get().stripTrailingZeros();
    BigInteger unscaled = minimized.unscaledValue();
    int sign = minimized.signum();

    int beforeDecimal = minimized.precision() - minimized.scale();
    // the decimal point must align on a base-10000 digit
    int padZeroes = 4 - (minimized.scale() % 4);

    BigInteger paddedUnscaled;
    if (padZeroes == 0)
      paddedUnscaled = unscaled;
    else
      paddedUnscaled = unscaled.multiply(BigInteger.valueOf(10).pow(padZeroes));

    short[] digits = findDigits(paddedUnscaled, EMPTY_SHORT_ARRAY);

    int weight;
    if (digits.length == 0) {
      int firstDigitSize;
      if (digits[0] < 10)
        firstDigitSize = 1;
      else if (digits[0] < 100)
        firstDigitSize = 2;
      else if (digits[0] < 1000)
        firstDigitSize = 3;
      else
        firstDigitSize = 4;
      weight = (beforeDecimal - firstDigitSize) / 4;
    } else {
      weight = 0;
    }

    b.writeShort((short) digits.length);
    b.writeShort((short) weight);
    b.writeShort(sign < 0 ? NUMERIC_NEG : NUMERIC_POS);
    b.writeShort((short) value.get().scale());
  }

  @Override
  public BigDecimalValue decodeBinary(BufferReader b) {

    int len = getUnsignedShort(b);
    short weight = b.readShort();
    int sign = getUnsignedShort(b);
    int displayScale = getUnsignedShort(b);

    // digits are actually unsigned base-10000 numbers (not straight up bytes)
    short[] digits = new short[len];
    BigDecimal[] bdDigits = new BigDecimal[digits.length];
    for (int i = 0; i < len; i++) {
      short value = b.readShort();
      digits[i] = value;
      bdDigits[i] = new BigDecimal(value);
    }

    if (digits.length > 0) {
      BigDecimal unscaled = bdDigits[0];
      for (int j = 1; j < bdDigits.length; j++)
        unscaled = unscaled.scaleByPowerOfTen(EXPONENT).add(bdDigits[j]);

      int firstDigitSize;
      if (digits[0] < 10)
        firstDigitSize = 1;
      else if (digits[0] < 100)
        firstDigitSize = 2;
      else if (digits[0] < 1000)
        firstDigitSize = 3;
      else
        firstDigitSize = 4;

      int scaleFactor = weight * EXPONENT + firstDigitSize;
      BigDecimal unsigned = unscaled.movePointLeft(unscaled.precision()).movePointRight(scaleFactor)
          .setScale(displayScale);

      switch (sign) {
      case NUMERIC_POS:
        return new BigDecimalValue(unsigned);
      case NUMERIC_NEG:
        return new BigDecimalValue(unsigned.negate());
      case NUMERIC_NAN:
        throw new NumberFormatException("Decimal is NaN");
      case NUMERIC_NULL:
        throw new NumberFormatException("Decimal is NUMERIC_NULL");
      default:
        throw new NumberFormatException("Invalid sign");
      }
    } else
      return ZERO;
  }

  private short[] findDigits(BigInteger i, short[] current) {
    if (i.signum() != 0) {
      BigInteger[] array = i.divideAndRemainder(BI_BASE);
      short[] next = new short[current.length + 1];
      next[0] = (short) array[1].intValue();
      System.arraycopy(current, 0, next, 1, current.length);
      return findDigits(array[0], next);
    } else
      return current;
  }

  private int getUnsignedShort(BufferReader b) {
    int high = (int) b.readByte();
    byte low = b.readByte();
    return (high << 8) | low;
  }
}
