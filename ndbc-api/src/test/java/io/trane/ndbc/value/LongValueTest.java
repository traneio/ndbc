package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class LongValueTest {

	@Test
	public void getLong() {
		final Long value = 32L;
		final LongValue wrapper = new LongValue(value);
		assertEquals(value, wrapper.getLong());
	}

	@Test
	public void getBigDecimal() {
		final Long value = 44L;
		final LongValue wrapper = new LongValue(value);
		assertEquals(new BigDecimal(value), wrapper.getBigDecimal());
	}
}
