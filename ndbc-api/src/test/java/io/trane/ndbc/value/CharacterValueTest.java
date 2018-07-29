package io.trane.ndbc.value;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CharacterValueTest {

	@Test
	public void getCharacter() {
		final Character value = 'j';
		final CharacterValue wrapper = new CharacterValue(value);
		assertEquals(value, wrapper.getCharacter());
	}

	@Test
	public void getString() {
		final Character value = 'j';
		final CharacterValue wrapper = new CharacterValue(value);
		assertEquals(value.toString(), wrapper.getString());
	}

	@Test
	public void getBooleanTrue1() {
		final Character value = '1';
		final CharacterValue wrapper = new CharacterValue(value);
		assertEquals(true, wrapper.getBoolean());
	}

	@Test
	public void getBooleanTrueT() {
		final Character value = 'T';
		final CharacterValue wrapper = new CharacterValue(value);
		assertEquals(true, wrapper.getBoolean());
	}

	@Test
	public void getBooleanFalse0() {
		final Character value = '0';
		final CharacterValue wrapper = new CharacterValue(value);
		assertEquals(false, wrapper.getBoolean());
	}

	@Test
	public void getBooleanFalseF() {
		final Character value = 'F';
		final CharacterValue wrapper = new CharacterValue(value);
		assertEquals(false, wrapper.getBoolean());
	}
}
