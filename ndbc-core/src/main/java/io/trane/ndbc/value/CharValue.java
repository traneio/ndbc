package io.trane.ndbc.value;

public final class CharValue extends Value<Character> {

  public CharValue(final Character value) {
    super(value);
  }

  @Override
  public final Character getCharacter() {
    return get();
  }
}
