package io.trane.ndbc.value;

public final class ByteArrayValue extends Value<byte[]> {

	public ByteArrayValue(final byte[] value) {
		super(value);
	}

	@Override
	public final byte[] getByteArray() {
		return get();
	}
}
