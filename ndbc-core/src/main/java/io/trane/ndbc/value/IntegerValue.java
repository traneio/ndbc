package io.trane.ndbc.value;

public class IntegerValue extends Value<Integer> {
  public IntegerValue(Integer value) {
    super(value);
  }

  public Integer getInteger() {
    return get();
  }
}