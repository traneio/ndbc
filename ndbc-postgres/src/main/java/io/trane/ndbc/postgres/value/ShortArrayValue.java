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
}