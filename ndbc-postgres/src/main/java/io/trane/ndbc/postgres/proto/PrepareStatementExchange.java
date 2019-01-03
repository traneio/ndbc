package io.trane.ndbc.postgres.proto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.trane.ndbc.postgres.proto.Message.Parse;
import io.trane.ndbc.postgres.proto.Message.Sync;
import io.trane.ndbc.postgres.proto.marshaller.Marshallers;
import io.trane.ndbc.postgres.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.util.PositionalQuery;
import io.trane.ndbc.value.Value;

public class PrepareStatementExchange {

  private final Sync          sync     = new Sync();
  private final Set<Integer>  prepared = new HashSet<>();
  private final Marshallers   marshallers;
  private final Unmarshallers unmarshallers;

  public PrepareStatementExchange(final Marshallers marshallers, final Unmarshallers unmarshallers) {
    this.marshallers = marshallers;
    this.unmarshallers = unmarshallers;
  }

  public final Exchange<String> apply(final String query, final List<Value<?>> params) {
    final int id = id(query, params);
    final String idString = Integer.toString(id);
    if (prepared.contains(id))
      return Exchange.value(idString);
    else
      return Exchange.send(marshallers.parse, new Parse(Integer.toString(id), PositionalQuery.apply(query), params))
          .thenSend(marshallers.sync, sync)
          .thenReceive(unmarshallers.parseComplete)
          .thenWaitFor(unmarshallers.readyForQuery)
          .onSuccess(ign -> Exchange.value(prepared.add(id)))
          .map(v -> idString);
  }

  private final int id(final String query, final List<Value<?>> params) {
    int id = query.hashCode();
    for (final Value<?> v : params)
      id = (31 * id) + v.getClass().hashCode();
    return id;
  }
}
