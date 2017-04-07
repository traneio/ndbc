package io.trane.ndbc.postgres.column;

public class IntColumn {

  public static String encode(int i) {
    return Integer.toString(i);
  }

  public static int decode(String s) {
    return Integer.parseInt(s);
  }
}
