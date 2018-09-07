package io.trane.ndbc.postgres.encoding;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Assert;

import io.trane.ndbc.value.LocalDateTimeArrayValue;

public class LocalDateTimeArrayEncodingTest extends EncodingTest<LocalDateTimeArrayValue, LocalDateTimeArrayEncoding> {

  public LocalDateTimeArrayEncodingTest() {
    super(new LocalDateTimeArrayEncoding(new LocalDateTimeEncoding(UTF8), UTF8), Oid.TIMESTAMP_ARRAY,
        LocalDateTimeArrayValue.class, r -> {
          final LocalDateTime[] localDateTimes = new LocalDateTime[r.nextInt(8)];
          Arrays.setAll(localDateTimes, p -> randomLocalDateTime(r));
          return new LocalDateTimeArrayValue(localDateTimes);
        }, (a, b) -> Assert.assertArrayEquals(a.getLocalDateTimeArray(), b.getLocalDateTimeArray()));
  }
}