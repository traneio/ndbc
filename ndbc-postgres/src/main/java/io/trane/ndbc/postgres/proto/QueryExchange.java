package io.trane.ndbc.postgres.proto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import io.trane.ndbc.ResultSet;
import io.trane.ndbc.Row;
import io.trane.ndbc.postgres.encoding.Format;
import io.trane.ndbc.postgres.encoding.ValueEncoding;
import io.trane.ndbc.postgres.proto.Message.CommandComplete;
import io.trane.ndbc.postgres.proto.Message.DataRow;
import io.trane.ndbc.postgres.proto.Message.EmptyQueryResponse;
import io.trane.ndbc.postgres.proto.Message.ReadyForQuery;
import io.trane.ndbc.postgres.proto.Message.RowDescription;
import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.PartialFunction;
import io.trane.ndbc.value.Value;

public abstract class QueryExchange {

  protected final ValueEncoding encoding;

  public QueryExchange(ValueEncoding encoding) {
    super();
    this.encoding = encoding;
  }

  private final ResultSet emptyResultSet = new ResultSet(new Iterator<Row>() {
    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public Row next() {
      throw new NoSuchElementException("Empty ResultSet");
    }
  });

  private final Row toRow(ValueEncoding encoding, RowDescription desc, DataRow data) {

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
      BufferReader reader = values[i];
      columns[i] = encoding.decode(field.dataType, Format.fromCode(field.formatCode), reader);
      reader.release();
    }

    return new Row(positions, columns);
  }

  protected final PartialFunction<ServerMessage, Exchange<ResultSet>> emptyQueryResponse = PartialFunction.when(
      EmptyQueryResponse.class, msg -> Exchange.value(emptyResultSet));

  protected final PartialFunction<ServerMessage, Exchange<Void>> commandComplete = PartialFunction.when(
      CommandComplete.class, msg -> Exchange.VOID);

  protected final PartialFunction<ServerMessage, Exchange<Void>> readyForQuery = PartialFunction.when(
      ReadyForQuery.class, msg -> Exchange.VOID);

  protected final Exchange<ResultSet> readQueryResult() {
    return Exchange.receive(rowDescription).flatMap(desc -> gatherDataRows(new ArrayList<>())
        .map(rows -> toResultSet(desc, rows)));
  }

  private final Exchange<List<DataRow>> gatherDataRows(List<DataRow> rows) {
    return Exchange.receive(PartialFunction.<ServerMessage, Exchange<List<DataRow>>>apply()
        .orElse(CommandComplete.class, msg -> Exchange.value(rows))
        .orElse(DataRow.class, row -> {
          rows.add(row);
          return gatherDataRows(rows);
        }));
  }

  private final ResultSet toResultSet(RowDescription desc, List<DataRow> dataRows) {
    final Iterator<Row> rows = dataRows.stream()
        .map(data -> toRow(encoding, desc, data)).iterator();
    return new ResultSet(rows);
  }

  private final PartialFunction<ServerMessage, Exchange<RowDescription>> rowDescription = PartialFunction.when(
      RowDescription.class, msg -> Exchange.value(msg));

}
