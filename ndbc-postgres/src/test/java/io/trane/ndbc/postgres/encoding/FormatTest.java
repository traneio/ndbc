package io.trane.ndbc.postgres.encoding;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FormatTest {

	@Test
	public void fromCode() {
		assertEquals(Format.TEXT, Format.fromCode((short) 0));
		assertEquals(Format.BINARY, Format.fromCode((short) 1));
	}

	@Test(expected = IllegalStateException.class)
	public void fromCodeInvalid() {
		Format.fromCode((short) 99);
	}

	@Test
	public void getCode() {
		assertEquals((short) 0, Format.TEXT.getCode());
		assertEquals((short) 1, Format.BINARY.getCode());
	}
}
