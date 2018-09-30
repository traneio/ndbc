package io.trane.ndbc.postgres.value;

import io.trane.ndbc.value.Value;

public final class CharacterArrayValue extends Value<Character[]> {

  public CharacterArrayValue(final Character[] value) {
    super(value);
  }

  @Override
  public final Character[] getCharacterArray() {
    return get();
  }
}