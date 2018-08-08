package io.trane.ndbc.mysql.proto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.trane.ndbc.mysql.proto.Message.ExecuteStatementCommand;
import io.trane.ndbc.mysql.proto.Message.PrepareStatementCommand;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedExchange {

  private final Map<String, Long> prepared = new HashMap<>();

  private final Marshallers    marshallers;
  private final Exchange<Long> readStatementId;

  public ExtendedExchange(Marshallers marshallers, Exchange<Long> affectedRows) {
    this.marshallers = marshallers;
    this.readStatementId = affectedRows;
  }

  public final <T> Exchange<T> apply(final String query, final List<Value<?>> params, final Exchange<T> readResult) {
    return withParsing(query, params,
        id -> Exchange.send(marshallers.executeStatementCommand, new ExecuteStatementCommand(id))
            .thenWaitFor(readResult));
  }

  private final <T> Exchange<T> withParsing(final String query, final List<Value<?>> params,
      final Function<Long, Exchange<T>> f) {
    String positionalQuery = positional(query);
    Long statementId = prepared.get(positionalQuery);
    if (statementId != null)
      return f.apply(statementId);
    else
      return Exchange.send(marshallers.textCommand, new PrepareStatementCommand(query))
          .thenWaitFor(readStatementId)
          .onSuccess(id -> Exchange.value(prepared.put(positionalQuery, statementId)))
          .flatMap(f::apply);
  }

  // TODO handle quotes, comments, etc.
  private final String positional(final String query) {
    int idx = 0;
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < query.length(); i++) {
      final char c = query.charAt(i);
      if (c == '?') {
        idx += 1;
        sb.append("$");
        sb.append(idx);
      } else
        sb.append(c);
    }
    return sb.toString();
  }
}
