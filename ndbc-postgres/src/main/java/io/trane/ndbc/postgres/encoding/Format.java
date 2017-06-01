package io.trane.ndbc.postgres.encoding;

public enum Format {
  TEXT((short) 0), BINARY((short) 1);

  public static final Format fromCode(final short code) {
    if (code == 0)
      return TEXT;
    else if (code == 1)
      return BINARY;
    else
      throw new IllegalStateException("Invalid format code: " + code);
  }

  private final short code;

  private Format(final short code) {
    this.code = code;
  }

  public short getCode() {
    return code;
  }
}
