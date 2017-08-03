package io.trane.ndbc.value;

public final class ByteArrayArrayValue extends Value<byte[][]> {
  
  public ByteArrayArrayValue(final byte[][] value) {
    super(value);
  }

  @Override
  public final byte[][] getByteArrayArray() {
    return get();
  }
}