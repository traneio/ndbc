package io.trane.ndbc.value;

public final class IntegerArrayValue extends Value<Integer[]> {
  
  public static final IntegerArrayValue EMPTY = new IntegerArrayValue(new Integer[0]);
  
  public IntegerArrayValue(final Integer[] value) {
    super(value);
  }

  @Override
  public final Integer[] getIntegerArray() {
    return get();
  }
}