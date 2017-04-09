package io.trane.ndbc.postgres.proto;

import java.util.HashMap;
import java.util.Map;

import io.trane.ndbc.Value;
import io.trane.ndbc.postgres.encoding.Format;
import io.trane.ndbc.postgres.encoding.ValueEncoding;
import io.trane.ndbc.postgres.proto.Message.DataRow;
import io.trane.ndbc.postgres.proto.Message.RowDescription;
import io.trane.ndbc.proto.BufferReader;

class PostgresRow implements io.trane.ndbc.Row {

  protected static PostgresRow apply(ValueEncoding encoding, RowDescription desc, DataRow data) {

    RowDescription.Field[] fields = desc.fields;
    BufferReader[] values = data.values;

    int length = fields.length;
    if (length != values.length)
      throw new IllegalStateException("Invalid number of columns.");

    Map<String, Integer> positions = new HashMap<>(length);
    Value<?>[] columns = new Value<?>[length];

    for (int i = 0; i < length; i++) {
      RowDescription.Field field = fields[i];
      positions.put(field.name, i);
      columns[i] = encoding.decode(field.dataType, Format.fromCode(field.formatCode), values[i]);
    }

    return new PostgresRow(positions, columns);
  }

  private final Map<String, Integer> positions;
  private final Value<?>[] columns;

  private PostgresRow(Map<String, Integer> positions, Value<?>[] columns) {
    this.positions = positions;
    this.columns = columns;
  }

  @Override
  public String getString(int position) {
    return getValue(position).getString();
  }

  @Override
  public String getString(String name) {
    return getValue(name).getString();
  }

  @Override
  public Integer getInteger(int position) {
    return getValue(position).getInteger();
  }

  @Override
  public Integer getInteger(String name) {
    return getValue(name).getInteger();
  }

  @Override
  public Value<?> getValue(int position) {
    return columns[position];
  }

  @Override
  public Value<?> getValue(String name) {
    return columns[positions.get(name)];
  }
}
