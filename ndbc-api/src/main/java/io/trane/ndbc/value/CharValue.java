package io.trane.ndbc.value;

public final class CharValue extends Value<Character> {

  public CharValue(final Character value) {
    super(value);
  }

  @Override
  public final Character getCharacter() {
    return get();
  }

  @Override
  public final Boolean getBoolean() {
    return get() == '1' || get() == 'T';
  }
}
