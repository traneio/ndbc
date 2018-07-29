package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.value.LocalDateTimeValue;

public class LocalDateTimeEncodingTest extends EncodingTest<LocalDateTimeValue, LocalDateTimeEncoding> {

	public LocalDateTimeEncodingTest() {
		super(new LocalDateTimeEncoding(), Oid.TIMESTAMP, LocalDateTimeValue.class,
				r -> new LocalDateTimeValue(randomLocalDateTime(r)));
	}
}
