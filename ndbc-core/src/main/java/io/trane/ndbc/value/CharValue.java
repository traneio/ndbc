package io.trane.ndbc.value;

public class CharValue extends Value<Character> {

  public CharValue(Character value) {
    super(value);
  }

  @Override
  public Character getCharacter() {
    return get();
  }
}
