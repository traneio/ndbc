package io.trane.ndbc.value;

public final class IntegerArrayValue extends Value<Value<?>[]> {
  
  public static final IntegerArrayValue EMPTY = new IntegerArrayValue(new Value[0]);
  
  public IntegerArrayValue(final Value<?>[] value) {
    super(value);
  }

  @Override
  public final Integer[] getIntegerArray() {
    Value<?>[] v = get();
    Integer[] r = new Integer[v.length];
    for(int i = 0; i < v.length; i++) {
      r[i] = v[i].getInteger();
    }
    return r;
  }
  
  @Override
  public final Value<?>[] getValueArray() {
    return get();
  }

//  @Override
//  public final BigDecimal getBigDecimal() {
//    return new BigDecimal(get());
//  }
//
//  @Override
//  public final Double getDouble() {
//    return new Double(get());
//  }
//
//  @Override
//  public final Float getFloat() {
//    return new Float(get());
//  }
//
//  @Override
//  public final Long getLong() {
//    return new Long(get());
//  }
//
//  @Override
//  public final Boolean getBoolean() {
//    return get() == 1;
//  }
}