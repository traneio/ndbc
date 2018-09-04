package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;

import io.trane.ndbc.value.FloatValue;

public class FloatEncodingTest extends EncodingTest<FloatValue, FloatEncoding> {

	public FloatEncodingTest() {
		super(new FloatEncoding(Charset.forName("UTF-8")), Oid.FLOAT4, FloatValue.class,
				r -> new FloatValue(r.nextFloat()));
	}

}
