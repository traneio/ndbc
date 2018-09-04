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

  private final ExtendedExchange    extendedExchange;
  private final Exchange<List<Row>> resultSet;

  public ExtendedQueryExchange(ExtendedExchange extendedExchange, Exchange<List<Row>> resultSet) {
    this.extendedExchange = extendedExchange;
    this.resultSet = resultSet;
  }

  @Override
  public Exchange<List<Row>> apply(final String command, final List<Value<?>> params) {
    return extendedExchange.apply(command, params, resultSet);
  }
}
