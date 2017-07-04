package io.trane.ndbc.postgres.encoding;

import static io.trane.ndbc.util.Collections.toImmutableSet;

import io.trane.ndbc.value.ShortValue;

public class ShortEncodingTest extends EncodingTest<ShortValue, ShortEncoding> {

  public ShortEncodingTest() {
    super(
        new ShortEncoding(),
        toImmutableSet(Oid.INT2),
        ShortValue.class,
        r -> new ShortValue((short) r.nextInt()));
  }

}
