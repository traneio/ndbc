package io.trane.ndbc.postgres.encoding;

import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Arrays;

import org.junit.Assert;

import io.trane.ndbc.postgres.value.OffsetTimeArrayValue;

public class OffsetTimeArrayEncodingTest extends EncodingTest<OffsetTimeArrayValue, OffsetTimeArrayEncoding> {

  public OffsetTimeArrayEncodingTest() {
    super(new OffsetTimeArrayEncoding(new OffsetTimeEncoding(UTF8), UTF8), Oid.TIMETZ_ARRAY, OffsetTimeArrayValue.class,
        r -> {
          final OffsetTime[] offsetTimes = new OffsetTime[r.nextInt(8)];
          Arrays.setAll(offsetTimes,
              p -> randomLocalDateTime(r).toLocalTime().atOffset(ZoneOffset.ofTotalSeconds(r.nextInt(18 * 2) + 18)));
          return new OffsetTimeArrayValue(offsetTimes);
        }, (a, b) -> Assert.assertArrayEquals(a.getOffsetTimeArray(), b.getOffsetTimeArray()));
  }
}