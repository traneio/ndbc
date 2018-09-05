package io.trane.ndbc.value;

public final class StringValue extends Value<String> {
  public StringValue(final String value) {
    super(value);
  }

  @Override
  public final String getString() {
    return get();
  }
}
