package io.trane.ndbc.mysql.proto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.trane.ndbc.mysql.proto.Message.PrepareOk;
import io.trane.ndbc.mysql.proto.Message.PrepareStatementCommand;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.mysql.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.util.PositionalQuery;
import io.trane.ndbc.value.Value;

public class PrepareStatementExchange {

  private final Marshallers         marshallers;
  private final Unmarshallers       unmarshallers;
  private final Exchange<PrepareOk> readPrepareOk;
  private final Exchange<Void>      readOk;

  public PrepareStatementExchange(Marshallers marshallers, Unmarshallers unmarshallers,
      Exchange<PrepareOk> readPrepareOk, Exchange<Void> readOk) {
    this.marshallers = marshallers;
    this.unmarshallers = unmarshallers;
    this.readPrepareOk = readPrepareOk;
    this.readOk = readOk;
  }

  private final Map<String, Long> prepared = new HashMap<>();

  public final Exchange<Long> apply(final String query, final List<Value<?>> params) {
    final String positionalQuery = PositionalQuery.apply(query);
    final Long statementId = prepared.get(positionalQuery);
    if (statementId != null)
      return Exchange.value(statementId);
    else
      return Exchange.send(marshallers.textCommand, new PrepareStatementCommand(query)).then(readPrepareOk)
          .flatMap(ok -> readFields(ok.numOfParameters, ok.numOfParameters)
              .flatMap(ig -> readFields(ok.numOfColumns, ok.numOfColumns)).map(v -> ok.statementId))
          .onSuccess(id -> Exchange.value(prepared.put(positionalQuery, statementId)));
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

}
