package io.trane.ndbc.postgres.proto;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import io.trane.ndbc.Type;
import io.trane.ndbc.postgres.proto.Message.DataRow;
import io.trane.ndbc.postgres.proto.Message.RowDescription;

class PostgresRow implements io.trane.ndbc.Row {

  protected static PostgresRow apply(Charset charset, RowDescription desc, DataRow data) {

    RowDescription.Field[] fields = desc.fields;
    byte[][] values = data.values;

    int length = fields.length;
    if (length != values.length)
      throw new IllegalStateException("Invalid number of columns.");

    Map<String, Integer> positions = new HashMap<>(length);
    Column[] columns = new Column[length];

    for (int i = 0; i < length; i++) {
      RowDescription.Field field = fields[i];
      positions.put(field.name, i);
      // TODO Type
      columns[i] = new Column(charset, null, Format.values()[field.formatCode], values[i]);
    }

    return new PostgresRow(positions, columns);
  }

  private final Map<String, Integer> positions;
  private final Column[] columns;

  private PostgresRow(Map<String, Integer> positions, Column[] columns) {
    this.positions = positions;
    this.columns = columns;
  }

  @Override
  public Type getType(int position) {
    return getColumn(position).getType();
  }

  @Override
  public Type getType(String name) {
    return getColumn(name).getType();
  }

  @Override
  public String getString(int position) {
    return getColumn(position).getString();
  }

  @Override
  public String getString(String name) {
    return getColumn(name).getString();
  }

  @Override
  public int getInt(int position) {
    return getColumn(position).getInt();
  }

  @Override
  public int getInt(String name) {
    return getColumn(name).getInt();
  }

  private Column getColumn(int position) {
    return columns[position];
  }

  private Column getColumn(String name) {
    return columns[positions.get(name)];
  }

}

class Column {

  private final Charset charset;
  private final Type type;
  private final Format format;
  private final byte[] data;

  public Column(Charset charset, Type type, Format format, byte[] data) {
    super();
    this.charset = charset;
    this.type = type;
    this.format = format;
    this.data = data;
  }

  public Type getType() {
    return type;
  }

  public String getString() {
    return new String(data, charset);
  }

  public int getInt() {
    return Integer.parseInt(getString());
  }
}
