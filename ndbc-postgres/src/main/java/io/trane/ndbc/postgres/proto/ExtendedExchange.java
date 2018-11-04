package io.trane.ndbc.postgres.proto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import io.trane.ndbc.postgres.encoding.Format;
import io.trane.ndbc.postgres.proto.Message.Bind;
import io.trane.ndbc.postgres.proto.Message.Close;
import io.trane.ndbc.postgres.proto.Message.Describe;
import io.trane.ndbc.postgres.proto.Message.Execute;
import io.trane.ndbc.postgres.proto.Message.Parse;
import io.trane.ndbc.postgres.proto.Message.Sync;
import io.trane.ndbc.postgres.proto.marshaller.Marshallers;
import io.trane.ndbc.postgres.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.util.PositionalQuery;
import io.trane.ndbc.value.Value;

public final class ExtendedExchange {

  private final short[]       binary   = { Format.BINARY.getCode() };
  private final Sync          sync     = new Sync();
  private final Set<Integer>  prepared = new HashSet<>();
  private final Marshallers   marshallers;
  private final Unmarshallers unmarshallers;

  public ExtendedExchange(final Marshallers marshallers, final Unmarshallers unmarshallers) {
    this.marshallers = marshallers;
    this.unmarshallers = unmarshallers;
  }

  public final <T> Exchange<T> apply(final String query, final List<Value<?>> params, final Exchange<T> readResult) {
    return withParsing(query, params,
        id -> Exchange.send(marshallers.bind, new Bind(id, id, binary, params, binary))
            .thenSend(marshallers.describe, new Describe.DescribePortal(id))
            .thenSend(marshallers.execute, new Execute(id, 0))
            .thenSend(marshallers.close, new Close.ClosePortal(id)).thenSend(marshallers.sync, sync))
                .thenReceive(unmarshallers.bindComplete).then(readResult)
                .thenReceive(unmarshallers.closeComplete).thenWaitFor(unmarshallers.readyForQuery);
  }

  private final <T> Exchange<T> withParsing(final String query, final List<Value<?>> params,
      final Function<String, Exchange<T>> f) {
    final int id = id(query, params);
    final String idString = Integer.toString(id);
    if (prepared.contains(id))
      return f.apply(idString);
    else
      return Exchange.send(marshallers.parse, new Parse(Integer.toString(id), PositionalQuery.apply(query), params))
          .then(f.apply(idString)).thenReceive(unmarshallers.parseComplete)
          .onSuccess(ign -> Exchange.value(prepared.add(id)));
  }

  private final int id(final String query, final List<Value<?>> params) {
    int id = query.hashCode();
    for (final Value<?> v : params)
      id = (31 * id) + v.getClass().hashCode();
    return id;
  }
}
