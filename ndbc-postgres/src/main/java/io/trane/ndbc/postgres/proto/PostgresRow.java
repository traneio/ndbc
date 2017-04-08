package io.trane.ndbc.postgres.proto;

import java.util.HashMap;
import java.util.Map;

import io.trane.ndbc.Type;
import io.trane.ndbc.postgres.encoding.Encodings;
import io.trane.ndbc.postgres.encoding.Format;
import io.trane.ndbc.postgres.proto.Message.DataRow;
import io.trane.ndbc.postgres.proto.Message.RowDescription;
import io.trane.ndbc.proto.BufferReader;

class PostgresRow implements io.trane.ndbc.Row {

  protected static PostgresRow apply(RowDescription desc, DataRow data) {

    RowDescription.Field[] fields = desc.fields;
    BufferReader[] values = data.values;

    int length = fields.length;
    if (length != values.length)
      throw new IllegalStateException("Invalid number of columns.");

    Map<String, Integer> positions = new HashMap<>(length);
    Column[] columns = new Column[length];

    for (int i = 0; i < length; i++) {
      RowDescription.Field field = fields[i];
      positions.put(field.name, i);
      // TODO Type
      columns[i] = new Column(null, Format.fromCode(field.formatCode), values[i]);
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

  private final Type type;
  private final Format format;
  private final BufferReader reader;

  public Column(Type type, Format format, BufferReader reader) {
    super();
    this.type = type;
    this.format = format;
    this.reader = reader;
  }

  public Type getType() {
    return type;
  }

  public String getString() {
    return Encodings.stringEncoding.decode(format, reader);
  }

  public int getInt() {
    return Encodings.intEncoding.decode(format, reader);
  }
}
