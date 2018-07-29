package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.value.DoubleValue;

public class DoubleEncodingTest extends EncodingTest<DoubleValue, DoubleEncoding> {

	public DoubleEncodingTest() {
		super(new DoubleEncoding(), Oid.FLOAT8, DoubleValue.class, r -> new DoubleValue(r.nextDouble()));
	}

}
