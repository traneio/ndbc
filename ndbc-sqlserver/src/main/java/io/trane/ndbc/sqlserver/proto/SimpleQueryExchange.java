package io.trane.ndbc.sqlserver.proto;

import java.util.List;
import java.util.function.Function;

import io.trane.ndbc.Row;
import io.trane.ndbc.proto.Exchange;

public class SimpleQueryExchange implements Function<String, Exchange<List<Row>>> {

  @Override
  public Exchange<List<Row>> apply(final String query) {
    return null;
  }
}
