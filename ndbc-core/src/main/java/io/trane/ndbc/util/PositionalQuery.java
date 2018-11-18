package io.trane.ndbc.util;

// TODO this should be a more complete parser. Example:
// https://github.com/pgjdbc/pgjdbc/blob/c2885dd0cfc793f81e5dd3ed2300bb32476eb14a/pgjdbc/src/main/java/org/postgresql/core/Parser.java#L44
public class PositionalQuery {

  public static final String apply(final String query) {
    int idx = 0;
    final StringBuilder sb = new StringBuilder();

    for (int i = 0; i < query.length(); i++) {
      final char c = query.charAt(i);
      if (c == '?') {
        // escape ??
        if ((query.length() > (i + 1)) && (query.charAt(i + 1) == '?')) {
          sb.append("?");
          i++;
        } else {
          idx += 1;
          sb.append("$");
          sb.append(idx);
        }
      } else
        sb.append(c);
    }
    return sb.toString();
  }
}
