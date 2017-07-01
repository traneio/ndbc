package io.trane.ndbc.postgres.proto;

import java.util.List;

import io.trane.ndbc.Row;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedQueryExchange {

  private final QueryResultExchange queryResultExchange;
  private final ExtendedExchange    extendedExchange;

  public ExtendedQueryExchange(final QueryResultExchange queryResultExchange,
      final ExtendedExchange extendedExchange) {
    super();
    this.queryResultExchange = queryResultExchange;
    this.extendedExchange = extendedExchange;
  }

  public final Exchange<List<Row>> apply(final String query, final List<Value<?>> params) {
    return extendedExchange.apply(query, params, queryResultExchange.apply());
  }
}
