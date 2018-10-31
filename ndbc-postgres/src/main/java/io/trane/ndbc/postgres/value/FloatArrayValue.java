package io.trane.ndbc.postgres.value;

public final class FloatArrayValue extends PostgresValue<Float[]> {

  public FloatArrayValue(final Float[] value) {
    super(value);
  }

  @Override
  public final Float[] getFloatArray() {
    return get();
  }
}