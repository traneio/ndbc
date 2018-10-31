package io.trane.ndbc.postgres.encoding;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Assert;

import io.trane.ndbc.postgres.value.LocalDateArrayValue;

public class LocalDateArrayEncodingTest extends EncodingTest<LocalDateArrayValue, LocalDateArrayEncoding> {

  public LocalDateArrayEncodingTest() {
    super(new LocalDateArrayEncoding(new LocalDateEncoding(UTF8), UTF8), Oid.DATE_ARRAY, LocalDateArrayValue.class,
        r -> {
          final LocalDate[] localDates = new LocalDate[r.nextInt(8)];
          Arrays.setAll(localDates, p -> randomLocalDateTime(r).toLocalDate());
          return new LocalDateArrayValue(localDates);
        }, (a, b) -> Assert.assertArrayEquals(a.get(), b.get()));
  }
}