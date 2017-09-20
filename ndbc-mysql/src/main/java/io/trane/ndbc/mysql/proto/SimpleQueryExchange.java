package io.trane.ndbc.mysql.proto;

import io.trane.ndbc.Row;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.util.PartialFunction;
import io.trane.ndbc.value.StringValue;
import io.trane.ndbc.value.Value;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.trane.ndbc.mysql.proto.Message.*;

public class SimpleQueryExchange implements Function<String, Exchange<List<Row>>>  {

  @Override
  public Exchange<List<Row>> apply(String sql) {
    return Exchange
            .send(new QueryCommand(sql))
            .thenReceive(PartialFunction.when(ResultSet.class, this::handleResultSet))
            .onFailure(ex -> Exchange.CLOSE);
  }

  private Exchange<List<Row>> handleResultSet(ResultSet rs) {
    AtomicInteger index = new AtomicInteger();
    Map<String, Integer> positions = rs.fields.stream().collect(Collectors.toMap(t -> t.name, any -> index.getAndIncrement()));
    List<Row> rows = rs.textRows.stream().map(row -> Row.apply(positions, textRowToValues(row))).collect(Collectors.toList());
    return Exchange.value(rows);
  }

  private Value<?>[] textRowToValues(TextRow textRow) {
    Value<?>[] values = new Value<?>[textRow.values.size()];
    for (int i = 0; i < values.length; i++) {
      values[i] = new StringValue(textRow.values.get(i));
    }
    return values;
  }
}
