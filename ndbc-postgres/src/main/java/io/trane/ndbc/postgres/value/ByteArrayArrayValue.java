package io.trane.ndbc.postgres.value;

public final class ByteArrayArrayValue extends PostgresValue<byte[][]> {

  public ByteArrayArrayValue(final byte[][] value) {
    super(value);
  }

  @Override
  public final byte[][] getByteArrayArray() {
    return get();
  }
}