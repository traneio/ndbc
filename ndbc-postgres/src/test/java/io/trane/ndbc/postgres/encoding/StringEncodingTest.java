package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.value.StringValue;

public class StringEncodingTest extends EncodingTest<StringValue, StringEncoding> {

  public StringEncodingTest() {
    super(new StringEncoding(UTF8), Oid.VARCHAR, StringValue.class, r -> {
      return new StringValue(randomString(r));
    });
  }
}