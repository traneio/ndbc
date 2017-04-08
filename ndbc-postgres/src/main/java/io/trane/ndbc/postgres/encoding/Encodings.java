package io.trane.ndbc.postgres.encoding;

public class Encodings {

  public static final Encoding<String> stringEncoding = new StringEncoding();
  public static final Encoding<Integer> intEncoding = new IntegerEncoding();
}
