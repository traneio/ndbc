package io.trane.ndbc.postgres.value;

public final class BooleanArrayValue extends PostgresValue<Boolean[]> {

  public BooleanArrayValue(final Boolean[] value) {
    super(value);
  }

  @Override
  public final Boolean[] getBooleanArray() {
    return get();
  }
}
