package io.trane.ndbc.postgres.value;

public final class ShortArrayValue extends PostgresValue<Short[]> {

  public static final ShortArrayValue EMPTY = new ShortArrayValue(new Short[0]);

  public ShortArrayValue(final Short[] value) {
    super(value);
  }

  @Override
  public final Short[] getShortArray() {
    return get();
  }

  @Override
  public byte[] getByteArray() {
    Short[] s = get();
    byte[] bytes = new byte[s.length];
    for (int i = 0; i < s.length; i++)
      bytes[i] = s[i].byteValue();
    return bytes;
  }

}