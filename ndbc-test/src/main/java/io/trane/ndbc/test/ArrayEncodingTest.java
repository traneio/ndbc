package io.trane.ndbc.test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;

import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.Config;
import io.trane.ndbc.value.Value;

public abstract class ArrayEncodingTest extends EncodingTest {

  public ArrayEncodingTest(Config config) {
    super(config);
  }

  @Test
  public void bigDecimalArray() throws CheckedFutureException {
    testArray(bigDecimalColumnTypes(), (ps, v) -> ps.setBigDecimalArray(v), Value::getBigDecimalArray, r -> {
      final BigDecimal[] array = new BigDecimal[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = BigDecimal.valueOf(r.nextLong(), r.nextInt(100));
      return array;
    });
  }

  @Test
  public void booleanArray() throws CheckedFutureException {
    testArray(booleanColumnTypes(), (ps, v) -> ps.setBooleanArray(v), Value::getBooleanArray, r -> {
      final Boolean[] array = new Boolean[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = r.nextBoolean();
      return array;
    });
  }

  @Test
  public void byteArrayArray() throws CheckedFutureException {
    testArray(byteArrayColumnTypes(), (ps, v) -> ps.setByteArrayArray(v), Value::getByteArrayArray, r -> {
      final byte[][] array = new byte[r.nextInt(10)][];
      for (int i = 0; i < array.length; i++) {
        final byte[] bytes = new byte[r.nextInt(5)];
        r.nextBytes(bytes);
        array[i] = bytes;
      }
      return array;
    });
  }

  @Test
  public void doubleArray() throws CheckedFutureException {
    testArray(doubleColumnTypes(), (ps, v) -> ps.setDoubleArray(v), Value::getDoubleArray, r -> {
      final Double[] array = new Double[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = r.nextDouble();
      return array;
    });
  }

  @Test
  public void floatArray() throws CheckedFutureException {
    testArray(floatColumnTypes(), (ps, v) -> ps.setFloatArray(v), Value::getFloatArray, r -> {
      final Float[] array = new Float[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = r.nextFloat();
      return array;
    });
  }

  @Test
  public void integerArray() throws CheckedFutureException {
    testArray(integerColumnTypes(), (ps, v) -> ps.setIntegerArray(v), Value::getIntegerArray, r -> {
      final Integer[] array = new Integer[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = r.nextInt();
      return array;
    });
  }

  @Test
  public void localDateArray() throws CheckedFutureException {
    testArray(localDateColumnTypes(), (ps, v) -> ps.setLocalDateArray(v), Value::getLocalDateArray, r -> {
      final LocalDate[] array = new LocalDate[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = randomLocalDateTime(r).toLocalDate();
      return array;
    });
  }

  @Test
  public void localDateTimeArray() throws CheckedFutureException {
    testArray(localDateTimeColumnTypes(), (ps, v) -> ps.setLocalDateTimeArray(v), Value::getLocalDateTimeArray, r -> {
      final LocalDateTime[] array = new LocalDateTime[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = randomLocalDateTime(r);
      return array;
    });
  }

  @Test
  public void localTimeArray() throws CheckedFutureException {
    testArray(localTimeColumnTypes(), (ps, v) -> ps.setLocalTimeArray(v), Value::getLocalTimeArray, r -> {
      final LocalTime[] array = new LocalTime[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = randomLocalDateTime(r).toLocalTime();
      return array;
    });
  }

  @Test
  public void longArray() throws CheckedFutureException {
    testArray(longColumnTypes(), (ps, v) -> ps.setLongArray(v), Value::getLongArray, r -> {
      final Long[] array = new Long[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = r.nextLong();
      return array;
    });
  }

  @Test
  public void offsetTimeArray() throws CheckedFutureException {
    testArray(offsetTimeColumnTypes(), (ps, v) -> ps.setOffsetTimeArray(v), Value::getOffsetTimeArray, r -> {
      final OffsetTime[] array = new OffsetTime[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = randomLocalDateTime(r).toLocalTime().atOffset(randomZoneOffset(r));
      return array;
    });
  }

  @Test
  public void shortArray() throws CheckedFutureException {
    testArray(shortColumnTypes(), (ps, v) -> ps.setShortArray(v), Value::getShortArray, r -> {
      final Short[] array = new Short[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = (short) r.nextInt();
      return array;
    });
  }

  @Test
  public void stringArray() throws CheckedFutureException {
    testArray(stringColumnTypes(), (ps, v) -> ps.setStringArray(v), Value::getStringArray, r -> {
      final String[] array = new String[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = radomString(r, 100);
      return array;
    });
  }
}
