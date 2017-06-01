package io.trane.ndbc.postgres.proto;

import io.trane.ndbc.ResultSet;
import io.trane.ndbc.postgres.proto.Message.Query;
import io.trane.ndbc.postgres.proto.Message.ReadyForQuery;
import io.trane.ndbc.proto.Exchange;

public final class SimpleQueryExchange {

  private final QueryResultExchange queryResultExchange;

  public SimpleQueryExchange(final QueryResultExchange queryResultExchange) {
    super();
    this.queryResultExchange = queryResultExchange;
  }

  public final Exchange<ResultSet> apply(final String query) {
    return Exchange.send(new Query(query)).then(queryResultExchange.apply()).thenWaitFor(ReadyForQuery.class);
  }
}
