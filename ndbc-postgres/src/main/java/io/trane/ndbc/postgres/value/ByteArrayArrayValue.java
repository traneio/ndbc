package io.trane.ndbc.postgres.value;

import io.trane.ndbc.value.Value;

public final class ByteArrayArrayValue extends Value<byte[][]> {

  public ByteArrayArrayValue(final byte[][] value) {
    super(value);
  }

  @Override
  public final byte[][] getByteArrayArray() {
    return get();
  }
}