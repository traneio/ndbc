package io.trane.ndbc.value;

public final class CharacterValue extends Value<Character> {

	public CharacterValue(final Character value) {
		super(value);
	}

	@Override
	public final Character getCharacter() {
		return get();
	}

	@Override
	public final String getString() {
		return get().toString();
	}

	@Override
	public final Boolean getBoolean() {
		final char c = get();
		return c == '1' || Character.toUpperCase(c) == 'T';
	}
}
