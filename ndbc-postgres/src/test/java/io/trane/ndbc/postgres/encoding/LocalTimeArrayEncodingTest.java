package io.trane.ndbc.postgres.encoding;

import java.time.LocalTime;
import java.util.Arrays;

import org.junit.Assert;

import io.trane.ndbc.postgres.value.LocalTimeArrayValue;

public class LocalTimeArrayEncodingTest extends EncodingTest<LocalTimeArrayValue, LocalTimeArrayEncoding> {

  public LocalTimeArrayEncodingTest() {
    super(new LocalTimeArrayEncoding(new LocalTimeEncoding(UTF8), UTF8), Oid.TIME_ARRAY, LocalTimeArrayValue.class,
        r -> {
          final LocalTime[] localTimes = new LocalTime[r.nextInt(8)];
          Arrays.setAll(localTimes, p -> randomLocalDateTime(r).toLocalTime());
          return new LocalTimeArrayValue(localTimes);
        }, (a, b) -> Assert.assertArrayEquals(a.getLocalTimeArray(), b.getLocalTimeArray()));
  }
}