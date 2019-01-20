package io.trane.ndbc.mysql.proto;

import java.util.List;
import java.util.function.Function;

import io.trane.ndbc.Row;
import io.trane.ndbc.mysql.proto.Message.QueryCommand;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.proto.Exchange;

public class SimpleQueryExchange implements Function<String, Exchange<List<Row>>> {

  private final Marshallers         marshallers;
  private final Exchange<List<Row>> resultSet;

  public SimpleQueryExchange(final Marshallers marshallers, final ResultSetExchange resultSetExchange) {
    this.marshallers = marshallers;
    this.resultSet = resultSetExchange.apply();
  }

  @Override
  public Exchange<List<Row>> apply(final String sql) {
    return Exchange.send(marshallers.textCommand, new QueryCommand(sql)).then(resultSet);
  }
}
