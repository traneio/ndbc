package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.value.StringValue;

public class StringEncodingTest extends EncodingTest<StringValue, StringEncoding> {

  public StringEncodingTest() {
    super(new StringEncoding(Charset.forName("UTF-8")), Oid.VARCHAR, StringValue.class, r -> {
      return new StringValue(randomString(r));
    });
  }
}