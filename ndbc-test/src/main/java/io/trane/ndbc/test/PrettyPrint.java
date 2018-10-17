package io.trane.ndbc.test;

import java.util.Arrays;

public interface PrettyPrint {

  public static String apply(final Object value) {
    if (value instanceof boolean[])
      return Arrays.toString((boolean[]) value);

    if (value instanceof byte[])
      return Arrays.toString((byte[]) value);

    if (value instanceof char[])
      return Arrays.toString((char[]) value);

    if (value instanceof short[])
      return Arrays.toString((short[]) value);

    if (value instanceof int[])
      return Arrays.toString((int[]) value);

    if (value instanceof double[])
      return Arrays.toString((double[]) value);

    if (value instanceof float[])
      return Arrays.toString((float[]) value);

    if (value instanceof long[])
      return Arrays.toString((long[]) value);

    if (value instanceof Object[])
      return Arrays.toString((Object[]) value);

    return String.valueOf(value);
  }
}
