package io.trane.ndbc.value;

public final class BooleanValue extends Value<Boolean> {

  public BooleanValue(final Boolean value) {
    super(value);
  }

  @Override
  public final Boolean getBoolean() {
    return get();
  }

  @Override
  public String getString() {
    return get() ? "T" : "F";
  }

  @Override
  public final Integer getInteger() {
    return get() ? 1 : 0;
  }

  @Override
  public final Short getShort() {
    return (short) (get() ? 1 : 0);
  }

  @Override
  public final Character getCharacter() {
    return get() ? 'T' : 'F';
  }
}
