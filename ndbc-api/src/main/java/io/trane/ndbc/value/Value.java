package io.trane.ndbc.value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.UUID;

public abstract class Value<T> {

  public static final NullValue NULL = new NullValue();

  private final T               value;

  public Value(final T value) {
    super();
    this.value = value;
  }

  public T get() {
    return value;
  }

  public <U> U unsafeGetAs(final Class<U> cls) {
    return cls.cast(value);
  }

  public boolean isNull() {
    return false;
  }

  public Character getCharacter() {
    return cantRead("Character");
  }

  public String getString() {
    return cantRead("String");
  }

  public Integer getInteger() {
    return cantRead("Integer");
  }
  
  public Integer[] getIntegerArray() {
    return cantRead("Integer[]");
  }

  public Boolean getBoolean() {
    return cantRead("Boolean");
  }

  public Long getLong() {
    return cantRead("Long");
  }
  
  public Byte getByte() {
    return cantRead("Byte");
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
  
  public UUID getUUID() {
    return cantRead("UUID");
  }

  public Value<?>[] getValueArray() {
    return cantRead("Value<?>[]");
  }
  
  private final <U> U cantRead(final String type) {
    throw new UnsupportedOperationException("Can't read `" + this + "` as `" + type + "`");
  }

  @Override
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (value == null ? 0 : value.hashCode());
    return result;
  }

  @Override
  public final boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!getClass().isInstance(obj))
      return false;
    final Value<?> other = (Value<?>) obj;
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
