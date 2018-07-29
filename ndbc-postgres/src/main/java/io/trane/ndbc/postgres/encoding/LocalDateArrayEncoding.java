package io.trane.ndbc.postgres.encoding;

import java.time.LocalDate;

import io.trane.ndbc.value.LocalDateArrayValue;

final class LocalDateArrayEncoding extends ArrayEncoding<LocalDate, LocalDateArrayValue> {

	private final LocalDateEncoding localDateEncoding;
	private final LocalDate[] emptyArray = new LocalDate[0];

	public LocalDateArrayEncoding(LocalDateEncoding localDateEncoding) {
		this.localDateEncoding = localDateEncoding;
	}

	@Override
	public final Integer oid() {
		return Oid.DATE_ARRAY;
	}

	@Override
	public final Class<LocalDateArrayValue> valueClass() {
		return LocalDateArrayValue.class;
	}

	@Override
	protected LocalDate[] newArray(int length) {
		return new LocalDate[length];
	}

	@Override
	protected LocalDate[] emptyArray() {
		return emptyArray;
	}

	@Override
	protected Encoding<LocalDate, ?> itemEncoding() {
		return localDateEncoding;
	}

	@Override
	protected LocalDateArrayValue box(LocalDate[] value) {
		return new LocalDateArrayValue(value);
	}

	@Override
	protected LocalDate[] unbox(LocalDateArrayValue value) {
		return value.getLocalDateArray();
	}
}
