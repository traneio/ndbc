package io.trane.ndbc.postgres.value;

public final class DoubleArrayValue extends PostgresValue<Double[]> {

  public DoubleArrayValue(final Double[] value) {
    super(value);
  }

  @Override
  public final Double[] getDoubleArray() {
    return get();
  }

  @Override
  public Float[] getFloatArray() {
    final Double[] v = get();
    final Float[] f = new Float[v.length];
    for (int i = 0; i < v.length; i++)
      f[i] = v[i].floatValue();
    return f;
  }

}