package io.trane.ndbc.value;

public final class CharacterArrayValue extends Value<Character[]> {
  
  public CharacterArrayValue(final Character[] value) {
    super(value);
  }

  @Override
  public final Character[] getCharacterArray() {
    return get();
  }
}