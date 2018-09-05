package io.trane.ndbc.mysql.proto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.trane.ndbc.mysql.proto.Message.ExecuteStatementCommand;
import io.trane.ndbc.mysql.proto.Message.PrepareOk;
import io.trane.ndbc.mysql.proto.Message.PrepareStatementCommand;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.mysql.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedExchange {

  private final Map<String, Long> prepared = new HashMap<>();

  private final Marshallers         marshallers;
  private final Unmarshallers       unmarshallers;
  private final Exchange<PrepareOk> readPrepareOk;
  private final Exchange<Void>      readOk;

  public ExtendedExchange(final Marshallers marshallers, final Unmarshallers unmarshallers,
      final Exchange<PrepareOk> readPrepareOk,
      final Exchange<Void> readOk) {
    this.marshallers = marshallers;
    this.unmarshallers = unmarshallers;
    this.readPrepareOk = readPrepareOk;
    this.readOk = readOk;
  }

  public final <T> Exchange<T> apply(final String query, final List<Value<?>> params, final Exchange<T> readResult) {
    return withParsing(query, params,
        id -> Exchange.send(marshallers.executeStatementCommand, new ExecuteStatementCommand(id, params))
            .thenWaitFor(readResult));
  }

  private final <T> Exchange<T> withParsing(final String query, final List<Value<?>> params,
      final Function<Long, Exchange<T>> f) {
    final String positionalQuery = positional(query);
    final Long statementId = prepared.get(positionalQuery);
    if (statementId != null)
      return f.apply(statementId);
    else
      return Exchange.send(marshallers.textCommand, new PrepareStatementCommand(query)).then(readPrepareOk)
          .flatMap(ok -> readFields(ok.numOfParameters, ok.numOfParameters)
              .flatMap(ig -> readFields(ok.numOfColumns, ok.numOfColumns)).map(v -> ok.statementId))
          .onSuccess(id -> Exchange.value(prepared.put(positionalQuery, statementId))).flatMap(f::apply);
  }

  private final Exchange<Void> readFields(final int count, final int initialCount) {
    if (count == 0)
      if (initialCount == 0)
        return Exchange.VOID;
      else
        return readOk;
    else
      return Exchange.receive(unmarshallers.field).flatMap(v -> readFields(count - 1, initialCount));
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
