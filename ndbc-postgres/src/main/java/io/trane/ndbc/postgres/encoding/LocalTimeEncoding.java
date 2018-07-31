package io.trane.ndbc.postgres.encoding;

import java.time.LocalTime;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.LocalTimeValue;

final class LocalTimeEncoding extends Encoding<LocalTime, LocalTimeValue> {

	@Override
	public final Integer oid() {
		return Oid.TIME;
	}

	@Override
	public final Class<LocalTimeValue> valueClass() {
		return LocalTimeValue.class;
	}

	@Override
	public final String encodeText(final LocalTime value) {
		return value.toString();
	}

	@Override
	public final LocalTime decodeText(final String value) {
		return LocalTime.parse(value);
	}

	@Override
	public final void encodeBinary(final LocalTime value, final BufferWriter b) {
		b.writeLong(value.toNanoOfDay() / 1000);
	}

	@Override
	public final LocalTime decodeBinary(final BufferReader b) {
		return LocalTime.ofNanoOfDay(b.readLong() * 1000);
	}

	@Override
	protected LocalTimeValue box(final LocalTime value) {
		return new LocalTimeValue(value);
	}

	@Override
	protected LocalTime unbox(final LocalTimeValue value) {
		return value.getLocalTime();
	}
}
