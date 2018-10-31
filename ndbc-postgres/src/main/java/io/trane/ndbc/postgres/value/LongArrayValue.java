package io.trane.ndbc.postgres.value;

public final class LongArrayValue extends PostgresValue<Long[]> {

  public LongArrayValue(final Long[] value) {
    super(value);
  }

  @Override
  public final Long[] getLongArray() {
    return get();
  }
}