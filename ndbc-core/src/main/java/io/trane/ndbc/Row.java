package io.trane.ndbc;

import java.util.Map;
import java.util.Optional;

import io.trane.ndbc.value.Value;

public final class Row {

  private final Map<String, Integer> positions;
  private final Value<?>[] columns;

  public Row(Map<String, Integer> positions, Value<?>[] columns) {
    super();
    this.positions = positions;
    this.columns = columns;
  }

  public String getString(int position) {
    return getValue(position).getString();
  }

  public String getString(String name) {
    return getValue(name).getString();
  }

  public Optional<String> getOptionalString(int position) {
    return Optional.ofNullable(getString(position));
  }

  public Optional<String> getOptionalString(String name) {
    return Optional.ofNullable(getString(name));
  }

  public Integer getInteger(int position) {
    return getValue(position).getInteger();
  }

  public Integer getInteger(String name) {
    return getValue(name).getInteger();
  }

  public Optional<Integer> getOptionalInteger(int position) {
    return Optional.ofNullable(getInteger(position));
  }

  public Optional<Integer> getOptionalInteger(String name) {
    return Optional.ofNullable(getInteger(name));
  }

  public boolean isNull(int position) {
    return getValue(position).isNull();
  }

  public boolean isNull(String name) {
    return getValue(name).isNull();
  }

  public Value<?> getValue(int position) {
    return columns[position];
  }

  public Value<?> getValue(String name) {
    return columns[positions.get(name)];
  }
}
