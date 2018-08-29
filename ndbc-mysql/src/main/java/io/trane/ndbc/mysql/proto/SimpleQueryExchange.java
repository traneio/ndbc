package io.trane.ndbc.mysql.proto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.trane.ndbc.Row;
import io.trane.ndbc.mysql.proto.Message.QueryCommand;
import io.trane.ndbc.mysql.proto.Message.TextResultSet;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.proto.Exchange;

public class SimpleQueryExchange implements Function<String, Exchange<List<Row>>> {

  private final Marshallers             marshallers;
  private final Exchange<TextResultSet> textResultSet;

  public SimpleQueryExchange(Marshallers marshallers, Exchange<TextResultSet> textResultSet) {
    this.marshallers = marshallers;
    this.textResultSet = textResultSet;
  }

  @Override
  public Exchange<List<Row>> apply(final String sql) {
    return Exchange.send(marshallers.textCommand, new QueryCommand(sql))
        .then(textResultSet.map(this::handleResultSet));
  }

  private List<Row> handleResultSet(final TextResultSet rs) {
    final AtomicInteger index = new AtomicInteger();
    final Map<String, Integer> positions = rs.fields.stream()
        .collect(Collectors.toMap(t -> t.name, any -> index.getAndIncrement()));
    final List<Row> rows = rs.textRows.stream().map(row -> Row.apply(positions, row.values))
        .collect(Collectors.toList());
    return rows;
  }

}
