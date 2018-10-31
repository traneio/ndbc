package io.trane.ndbc.postgres.value;

public final class StringArrayValue extends PostgresValue<String[]> {

  public StringArrayValue(final String[] value) {
    super(value);
  }

  @Override
  public final String[] getStringArray() {
    return get();
  }
}