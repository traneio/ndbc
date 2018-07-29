package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.value.ByteArrayArrayValue;

final class ByteArrayArrayEncoding extends ArrayEncoding<byte[], ByteArrayArrayValue> {

	private final ByteArrayEncoding byteArrayEncoding;
	private final byte[][] emptyArray = new byte[0][0];

	public ByteArrayArrayEncoding(final ByteArrayEncoding byteArrayEncoding) {
		this.byteArrayEncoding = byteArrayEncoding;
	}

	@Override
	public final Integer oid() {
		return Oid.BYTEA_ARRAY;
	}

	@Override
	public final Class<ByteArrayArrayValue> valueClass() {
		return ByteArrayArrayValue.class;
	}

	@Override
	protected byte[][] newArray(final int length) {
		return new byte[length][];
	}

	@Override
	protected byte[][] emptyArray() {
		return emptyArray;
	}

	@Override
	protected Encoding<byte[], ?> itemEncoding() {
		return byteArrayEncoding;
	}

	@Override
	protected ByteArrayArrayValue box(final byte[][] value) {
		return new ByteArrayArrayValue(value);
	}

	@Override
	protected byte[][] unbox(final ByteArrayArrayValue value) {
		return value.getByteArrayArray();
	}
}
