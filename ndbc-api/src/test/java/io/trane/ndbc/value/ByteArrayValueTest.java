package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ByteArrayValueTest {

	@Test
	public void getByteArray() {
		final byte[] value = new byte[10];
		final ByteArrayValue wrapper = new ByteArrayValue(value);
		assertEquals(value, wrapper.getByteArray());
	}
}
