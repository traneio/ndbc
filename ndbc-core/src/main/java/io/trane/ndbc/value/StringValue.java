package io.trane.ndbc.value;

public class StringValue extends Value<String> {
  public StringValue(String value) {
    super(value);
  }

  @Override
  public String getString() {
    return get();
  }
}
