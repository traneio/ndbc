package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;
import java.time.ZoneOffset;

import io.trane.ndbc.value.OffsetTimeValue;

public class OffsetTimeEncodingTest extends EncodingTest<OffsetTimeValue, OffsetTimeEncoding> {

  public OffsetTimeEncodingTest() {
    super(new OffsetTimeEncoding(Charset.forName("UTF-8")), Oid.TIMETZ, OffsetTimeValue.class,
        r -> new OffsetTimeValue(randomLocalDateTime(r).toLocalTime()
            .atOffset(ZoneOffset.ofTotalSeconds(r.nextInt(18 * 2) + 18))));
  }
}
