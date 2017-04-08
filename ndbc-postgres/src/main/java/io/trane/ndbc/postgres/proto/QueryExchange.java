package io.trane.ndbc.postgres.proto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import io.trane.ndbc.ResultSet;
import io.trane.ndbc.Row;
import io.trane.ndbc.postgres.proto.Message.CommandComplete;
import io.trane.ndbc.postgres.proto.Message.DataRow;
import io.trane.ndbc.postgres.proto.Message.EmptyQueryResponse;
import io.trane.ndbc.postgres.proto.Message.ReadyForQuery;
import io.trane.ndbc.postgres.proto.Message.RowDescription;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.PartialFunction;

public abstract class QueryExchange {
  
  private final ResultSet emptyResultSet = new ResultSet() {
    @Override
    public Iterator<Row> iterator() {
      return new Iterator<Row>() {
        @Override
        public boolean hasNext() {
          return false;
        }

        @Override
        public PostgresRow next() {
          throw new NoSuchElementException("Empty ResultSet.");
        }
      };
    }
  };

  protected final PartialFunction<ServerMessage, Exchange<ResultSet>> emptyQueryResponse = PartialFunction.when(
      EmptyQueryResponse.class, msg -> Exchange.value(emptyResultSet));

  protected final PartialFunction<ServerMessage, Exchange<Void>> commandComplete = PartialFunction.when(
      CommandComplete.class, msg -> Exchange.VOID);

  protected final PartialFunction<ServerMessage, Exchange<Void>> readyForQuery = PartialFunction.when(
      ReadyForQuery.class, msg -> Exchange.VOID);

  protected final Exchange<ResultSet> readQueryResult() {
    return Exchange.receive(rowDescription).flatMap(desc -> gatherDataRows(new ArrayList<>())
        .map(rows -> toResultSet(desc, rows))
        .thenWaitFor(readyForQuery));
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
    final List<io.trane.ndbc.Row> rows = dataRows.stream()
        .map(data -> PostgresRow.apply(desc, data))
        .collect(Collectors.toList());
    return new ResultSet() {
      @Override
      public Iterator<io.trane.ndbc.Row> iterator() {
        return rows.iterator();
      }
    };
  }

  private final PartialFunction<ServerMessage, Exchange<RowDescription>> rowDescription = PartialFunction.when(
      RowDescription.class, msg -> Exchange.value(msg));

}
