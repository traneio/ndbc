package io.trane.ndbc.postgres.proto;

import java.util.List;
import java.util.function.Function;

import io.trane.ndbc.Row;
import io.trane.ndbc.postgres.proto.Message.Query;
import io.trane.ndbc.postgres.proto.Message.ReadyForQuery;
import io.trane.ndbc.proto.Exchange;

public final class SimpleQueryExchange implements Function<String, Exchange<List<Row>>> {

  private final QueryResultExchange queryResultExchange;

  public SimpleQueryExchange(final QueryResultExchange queryResultExchange) {
    super();
    this.queryResultExchange = queryResultExchange;
  }

  @Override
  public final Exchange<List<Row>> apply(final String query) {
    return Exchange.send(new Query(query)).then(queryResultExchange.apply())
        .thenWaitFor(ReadyForQuery.class);
  }
}
