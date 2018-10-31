package io.trane.ndbc.postgres.value;

public final class CharacterArrayValue extends PostgresValue<Character[]> {

  public CharacterArrayValue(final Character[] value) {
    super(value);
  }

  @Override
  public final Character[] getCharacterArray() {
    return get();
  }
}