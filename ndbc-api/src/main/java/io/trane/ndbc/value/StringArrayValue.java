package io.trane.ndbc.value;

public final class StringArrayValue extends Value<String[]> {
  
  public StringArrayValue(final String[] value) {
    super(value);
  }

  @Override
  public final String[] getStringArray() {
    return get();
  }
}