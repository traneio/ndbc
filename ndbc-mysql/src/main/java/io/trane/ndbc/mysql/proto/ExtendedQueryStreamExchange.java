package io.trane.ndbc.mysql.proto;

import java.util.List;
import java.util.function.BiFunction;

import io.trane.ndbc.Row;
import io.trane.ndbc.mysql.proto.ExtendedQueryStreamExchange.Fetch;
import io.trane.ndbc.mysql.proto.Message.ExecuteStatementCommand;
import io.trane.ndbc.mysql.proto.Message.FetchStatementCommand;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedQueryStreamExchange implements BiFunction<String, List<Value<?>>, Exchange<Fetch>> {

  private final PrepareStatementExchange prepareStatement;
  private final Exchange<List<Row>>      resultSet;
  private final Marshallers              marshallers;

  public ExtendedQueryStreamExchange(PrepareStatementExchange prepareStatement, Exchange<List<Row>> resultSet,
      Marshallers marshallers) {
    this.prepareStatement = prepareStatement;
    this.resultSet = resultSet;
    this.marshallers = marshallers;
  }

  public final class Fetch {
    private final long preparedStatementId;

    public Fetch(long preparedStatementId) {
      this.preparedStatementId = preparedStatementId;
    }

    public Exchange<List<Row>> apply(int size) {
      return Exchange.send(marshallers.fetchStatementCommand, new FetchStatementCommand(preparedStatementId, size))
          .then(resultSet);
    }
  }

  @Override
  public Exchange<Fetch> apply(final String query, final List<Value<?>> params) {
    return prepareStatement.apply(query, params).flatMap(
        id -> Exchange.send(marshallers.executeStatementCommand, new ExecuteStatementCommand(id, params, true))
            .then(Exchange.value(new Fetch(id))));
  }
}
