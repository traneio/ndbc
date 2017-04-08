package io.trane.ndbc;

public interface Row {

  Type getType(int position);

  Type getType(String name);

  String getString(int position);

  String getString(String name);

  int getInt(int position);

  int getInt(String name);
}
