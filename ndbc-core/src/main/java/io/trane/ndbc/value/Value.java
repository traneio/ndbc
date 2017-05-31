package io.trane.ndbc.value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;

public abstract class Value<T> {

  public static final NullValue NULL = new NullValue();

  private final T value;

  public Value(T value) {
    super();
    this.value = value;
  }

  public T get() {
    return value;
  }

  public boolean isNull() {
    return false;
  }

  private <U> U cantRead(String type) {
    throw new UnsupportedOperationException("Can't read " + get() + "as " + type);
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

  public Character getCharacter() {
    return cantRead("Integer");
  }
  
  public String getString() {
    return value == null ? "null" : value.toString();
  }

  public Integer getInteger() {
    return cantRead("Integer");
  }
  
  public Boolean getBoolean() {
    return cantRead("Boolean");
  }

  public Long getLong() {
    return cantRead("Long");
  }

  public Short getShort() {
    return cantRead("Long");
  }

  public BigDecimal getBigDecimal() {
    return cantRead("BigDecimal");
  }

  public Float getFloat() {
    return cantRead("Float");
  }

  public Double getDouble() {
    return cantRead("Double");
  }

  public LocalDateTime getLocalDateTime() {
    return cantRead("LocalDateTime");
  }

  public byte[] getByteArray() {
    return cantRead("ByteArray");
  }

  public LocalDate getLocalDate() {
    return cantRead("LocalDate");
  }

  public LocalTime getLocalTime() {
    return cantRead("LocalTime");
  }

  public OffsetTime getOffsetTime() {
    return cantRead("OffsetTime");
  }
}
