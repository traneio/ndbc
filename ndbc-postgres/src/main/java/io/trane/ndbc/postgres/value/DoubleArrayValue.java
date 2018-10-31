package io.trane.ndbc.postgres.value;

public final class DoubleArrayValue extends PostgresValue<Double[]> {

  public DoubleArrayValue(final Double[] value) {
    super(value);
  }

  @Override
  public final Double[] getDoubleArray() {
    return get();
  }
}