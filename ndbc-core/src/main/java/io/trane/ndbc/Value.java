package io.trane.ndbc;

public abstract class Value<T> {

  private final T value;

  public Value(T value) {
    super();
    this.value = value;
  }

  public T get() {
    return value;
  }

  public String getString() {
    return value == null ? null : value.toString();
  }

  public Integer getInteger() {
    return cantRead("Integer");
  }

  private <U> U cantRead(String type) {
    throw new UnsupportedOperationException("Can't read " + toString() + "as " + type);
  }

  public static class StringValue extends Value<String> {
    public StringValue(String value) {
      super(value);
    }

    @Override
    public String getString() {
      return super.value;
    }
  }

  public static class IntegerValue extends Value<Integer> {
    public IntegerValue(Integer value) {
      super(value);
    }

    public Integer getInteger() {
      return super.value;
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(getClass().isInstance(obj)))
      return false;
    Value<?> other = (Value<?>) obj;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + " [value=" + value + "]";
  }
}
