package io.trane.ndbc.postgres.encoding;

import java.time.OffsetTime;

import io.trane.ndbc.value.OffsetTimeArrayValue;

final class OffsetTimeArrayEncoding extends ArrayEncoding<OffsetTime, OffsetTimeArrayValue> {

	private final OffsetTimeEncoding offsetTimeEncoding;
	private final OffsetTime[] emptyArray = new OffsetTime[0];

	public OffsetTimeArrayEncoding(OffsetTimeEncoding offsetTimeEncoding) {
		this.offsetTimeEncoding = offsetTimeEncoding;
	}

	@Override
	public final Integer oid() {
		return Oid.TIMETZ_ARRAY;
	}

	@Override
	public final Class<OffsetTimeArrayValue> valueClass() {
		return OffsetTimeArrayValue.class;
	}

	@Override
	protected OffsetTime[] newArray(int length) {
		return new OffsetTime[length];
	}

	@Override
	protected OffsetTime[] emptyArray() {
		return emptyArray;
	}

	@Override
	protected Encoding<OffsetTime, ?> itemEncoding() {
		return offsetTimeEncoding;
	}

	@Override
	protected OffsetTimeArrayValue box(OffsetTime[] value) {
		return new OffsetTimeArrayValue(value);
	}

	@Override
	protected OffsetTime[] unbox(OffsetTimeArrayValue value) {
		return value.getOffsetTimeArray();
	}
}
