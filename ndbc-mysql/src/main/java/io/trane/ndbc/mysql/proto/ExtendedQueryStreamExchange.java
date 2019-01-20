package io.trane.ndbc.mysql.proto;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import io.trane.ndbc.Row;
import io.trane.ndbc.mysql.proto.ExtendedQueryStreamExchange.Fetch;
import io.trane.ndbc.mysql.proto.Message.ExecuteStatementCommand;
import io.trane.ndbc.mysql.proto.Message.FetchStatementCommand;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedQueryStreamExchange implements BiFunction<String, List<Value<?>>, Exchange<Fetch>> {

  private final PrepareStatementExchange prepareStatement;
  private final ResultSetExchange        resultSetExchange;
  private final Marshallers              marshallers;

  public ExtendedQueryStreamExchange(PrepareStatementExchange prepareStatement, ResultSetExchange resultSetExchange,
      Marshallers marshallers) {
    this.prepareStatement = prepareStatement;
    this.resultSetExchange = resultSetExchange;
    this.marshallers = marshallers;
  }

  public final class Fetch {
    private final long                          preparedStatementId;
    private final Supplier<Exchange<List<Row>>> readRows;

    public Fetch(long preparedStatementId, Supplier<Exchange<List<Row>>> readRows) {
      this.preparedStatementId = preparedStatementId;
      this.readRows = readRows;
    }

    public Exchange<List<Row>> apply(int size) {
      return Exchange.send(marshallers.fetchStatementCommand, new FetchStatementCommand(preparedStatementId, size))
          .then(readRows.get());
    }
  }

  @Override
  public Exchange<Fetch> apply(final String query, final List<Value<?>> params) {
    return prepareStatement.apply(query, params).flatMap(
        id -> Exchange.send(marshallers.executeStatementCommand, new ExecuteStatementCommand(id, params, true))
            .then(resultSetExchange.rows()).map(r -> new Fetch(id, r)));
  }
}
