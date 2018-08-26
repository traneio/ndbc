package io.trane.ndbc.mysql.proto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import io.trane.ndbc.Row;
import io.trane.ndbc.mysql.proto.Message.BinaryResultSet;
import io.trane.ndbc.mysql.proto.Message.BinaryRow;
import io.trane.ndbc.mysql.proto.Message.QueryCommand;
import io.trane.ndbc.mysql.proto.Message.TextResultSet;
import io.trane.ndbc.mysql.proto.Message.TextRow;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.StringValue;
import io.trane.ndbc.value.Value;

public final class ExtendedQueryExchange implements BiFunction<String, List<Value<?>>, Exchange<List<Row>>> {

  private final ExtendedExchange extendedExchange;
  private final Exchange<BinaryResultSet> binaryResultSet;

  public ExtendedQueryExchange(final ExtendedExchange extendedExchange, final Exchange<BinaryResultSet> binaryResultSet) {
    this.extendedExchange = extendedExchange;
    this.binaryResultSet = binaryResultSet;
  }

  @Override
  public Exchange<List<Row>> apply(final String command, final List<Value<?>> params) {
    return extendedExchange.apply(command, params, binaryResultSet.map(this::handleResultSet));
  }

  private List<Row> handleResultSet(final BinaryResultSet rs) {
    final AtomicInteger index = new AtomicInteger();
    final Map<String, Integer> positions = rs.fields.stream()
        .collect(Collectors.toMap(t -> t.name, any -> index.getAndIncrement()));
    final List<Row> rows = rs.binaryRows.stream().map(row -> Row.apply(positions, binaryRowToValues(row)))
        .collect(Collectors.toList());
    return rows;
  }

  private Value<?>[] binaryRowToValues(final BinaryRow binaryRow) {
    final Value<?>[] values = new Value<?>[binaryRow.values.length];
    for (int i = 0; i < values.length; i++) {
      values[i] = new StringValue(binaryRow.values[i]);
    }
    return values;
  }
}