package io.trane.ndbc.postgres.encoding;

import java.time.LocalDateTime;

import io.trane.ndbc.value.LocalDateTimeArrayValue;

final class LocalDateTimeArrayEncoding extends ArrayEncoding<LocalDateTime, LocalDateTimeArrayValue> {

	private final LocalDateTimeEncoding localDateTimeEncoding;
	private final LocalDateTime[] emptyArray = new LocalDateTime[0];

	public LocalDateTimeArrayEncoding(final LocalDateTimeEncoding localDateTimeEncoding) {
		this.localDateTimeEncoding = localDateTimeEncoding;
	}

	@Override
	public final Integer oid() {
		return Oid.TIMESTAMP_ARRAY;
	}

	@Override
	public final Class<LocalDateTimeArrayValue> valueClass() {
		return LocalDateTimeArrayValue.class;
	}

	@Override
	protected LocalDateTime[] newArray(final int length) {
		return new LocalDateTime[length];
	}

	@Override
	protected LocalDateTime[] emptyArray() {
		return emptyArray;
	}

	@Override
	protected Encoding<LocalDateTime, ?> itemEncoding() {
		return localDateTimeEncoding;
	}

	@Override
	protected LocalDateTimeArrayValue box(final LocalDateTime[] value) {
		return new LocalDateTimeArrayValue(value);
	}

	@Override
	protected LocalDateTime[] unbox(final LocalDateTimeArrayValue value) {
		return value.getLocalDateTimeArray();
	}
}
