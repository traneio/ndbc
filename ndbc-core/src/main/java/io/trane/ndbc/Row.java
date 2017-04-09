package io.trane.ndbc;

public interface Row {

  String getString(int position);

  String getString(String name);

  Integer getInteger(int position);

  Integer getInteger(String name);

  Value<?> getValue(int position);

  Value<?> getValue(String name);
}
