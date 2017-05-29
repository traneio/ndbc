package io.trane.ndbc.value;

public class ByteArrayValue extends Value<byte[]> {

  public ByteArrayValue(byte[] value) {
    super(value);
  }

  @Override
  public byte[] getByteArray() {
    return get();
  }
}
