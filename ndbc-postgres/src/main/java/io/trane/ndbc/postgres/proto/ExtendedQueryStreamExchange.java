package io.trane.ndbc.postgres.proto;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

import io.trane.ndbc.Row;
import io.trane.ndbc.postgres.encoding.Format;
import io.trane.ndbc.postgres.proto.ExtendedQueryStreamExchange.Fetch;
import io.trane.ndbc.postgres.proto.Message.Bind;
import io.trane.ndbc.postgres.proto.Message.Close;
import io.trane.ndbc.postgres.proto.Message.Describe;
import io.trane.ndbc.postgres.proto.Message.Execute;
import io.trane.ndbc.postgres.proto.Message.RowDescription;
import io.trane.ndbc.postgres.proto.Message.Sync;
import io.trane.ndbc.postgres.proto.marshaller.Marshallers;
import io.trane.ndbc.postgres.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedQueryStreamExchange implements BiFunction<String, List<Value<?>>, Exchange<Fetch>> {

  private final short[]                binary = { Format.BINARY.getCode() };
  private final Sync                   sync   = new Sync();
  private final Marshallers            marshallers;
  private final Unmarshallers          unmarshallers;
  private final PreparedStatementCache preparedStatementCache;
  private final QueryResultExchange    queryResultExchange;

  public final class Fetch {
    private final RowDescription desc;
    private final String         id;
    private final AtomicBoolean  hasNext = new AtomicBoolean(true);

    public Fetch(RowDescription desc, String id) {
      this.desc = desc;
      this.id = id;
    }

    public final Exchange<Void> close() {
      return Exchange.send(marshallers.close, new Close.ClosePortal(id))
          .thenSend(marshallers.sync, sync)
          .thenReceive(unmarshallers.closeComplete)
          .thenWaitFor(unmarshallers.readyForQuery);
    }

    public final boolean hasNext() {
      return hasNext.get();
    }

    public final Exchange<Optional<List<Row>>> fetch(int size) {
      if (hasNext.get())
        return Exchange.send(marshallers.execute, new Execute(id, size))
            .thenSend(marshallers.sync, sync)
            .then(queryResultExchange.apply(desc))
            .onSuccess(rows -> {
              hasNext.set(rows.size() == size);
              return Exchange.VOID;
            })
            .map(Optional::of);
      else
        return close().map(v -> Optional.empty());
    }
  }

  public ExtendedQueryStreamExchange(final Marshallers marshallers, final Unmarshallers unmarshallers,
      final PreparedStatementCache preparedStatementCache, QueryResultExchange queryResultExchange) {
    this.marshallers = marshallers;
    this.unmarshallers = unmarshallers;
    this.preparedStatementCache = preparedStatementCache;
    this.queryResultExchange = queryResultExchange;
  }

  public final Exchange<Fetch> apply(final String query, final List<Value<?>> params) {
    return preparedStatementCache.apply(query, params,
        id -> Exchange.send(marshallers.bind, new Bind(id, id, binary, params, binary))
            .thenSend(marshallers.describe, new Describe.DescribePortal(id))
            .thenSend(marshallers.sync, sync)
            .thenReceive(unmarshallers.bindComplete)
            .flatMap(v -> Exchange.receive(unmarshallers.rowDescription)
                .map(desc -> new Fetch(desc, id))));
  }

}
