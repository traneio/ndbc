package io.trane.ndbc.postgres.value;

public final class IntegerArrayValue extends PostgresValue<Integer[]> {

  public IntegerArrayValue(final Integer[] value) {
    super(value);
  }

  @Override
  public final Integer[] getIntegerArray() {
    return get();
  }
}