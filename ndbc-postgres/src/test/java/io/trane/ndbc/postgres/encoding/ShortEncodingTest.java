package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.value.ShortValue;

public class ShortEncodingTest extends EncodingTest<ShortValue, ShortEncoding> {

  public ShortEncodingTest() {
    super(new ShortEncoding(Charset.forName("UTF-8")), Oid.INT2, ShortValue.class,
        r -> new ShortValue((short) r.nextInt()));
  }
}
