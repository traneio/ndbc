package io.trane.ndbc.postgres.proto;

import java.util.List;
import java.util.function.BiFunction;

import io.trane.ndbc.Row;
import io.trane.ndbc.postgres.encoding.Format;
import io.trane.ndbc.postgres.proto.ExtendedQueryStreamExchange.Fetch;
import io.trane.ndbc.postgres.proto.Message.Bind;
import io.trane.ndbc.postgres.proto.Message.Close;
import io.trane.ndbc.postgres.proto.Message.Describe;
import io.trane.ndbc.postgres.proto.Message.Execute;
import io.trane.ndbc.postgres.proto.Message.Flush;
import io.trane.ndbc.postgres.proto.Message.RowDescription;
import io.trane.ndbc.postgres.proto.Message.Sync;
import io.trane.ndbc.postgres.proto.marshaller.Marshallers;
import io.trane.ndbc.postgres.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedQueryStreamExchange implements BiFunction<String, List<Value<?>>, Exchange<Fetch>> {

  private final short[]                  binary = { Format.BINARY.getCode() };
  private final Sync                     sync   = new Sync();
  private final Marshallers              marshallers;
  private final Unmarshallers            unmarshallers;
  private final PrepareStatementExchange prepareStatement;
  private final QueryResultExchange      queryResultExchange;

  public final class Fetch {
    private boolean        done = false;
    private RowDescription desc = null;
    private final String   id;

    public Fetch(String id) {
      this.id = id;
    }

    public final Exchange<List<Row>> fetch(int size) {
      if (desc == null)
        return Exchange.send(marshallers.execute, new Execute(id, size))
            .thenSend(marshallers.flush, new Flush())
            .thenReceive(unmarshallers.bindComplete)
            .then(Exchange.receive(unmarshallers.rowDescription)).map(d -> desc = d)
            .flatMap(queryResultExchange::apply);
      else
        return Exchange.send(marshallers.execute, new Execute(id, size))
            .thenSend(marshallers.flush, new Flush())
            .then(queryResultExchange.apply(desc))
            .flatMap(rows -> {
              if (rows.size() != size)
                return close().map(v -> rows);
              else
                return Exchange.value(rows);
            });
      // .thenWaitFor(unmarshallers.readyForQuery);
    }

    private final Exchange<Void> close() {
      return Exchange.send(marshallers.close, new Close.ClosePortal(id))
          .thenSend(marshallers.sync, sync)
          .thenReceive(unmarshallers.closeComplete)
          .thenWaitFor(unmarshallers.readyForQuery);
    }
  }

  public ExtendedQueryStreamExchange(final Marshallers marshallers, final Unmarshallers unmarshallers,
      final PrepareStatementExchange preparedStatementCache, QueryResultExchange queryResultExchange) {
    this.marshallers = marshallers;
    this.unmarshallers = unmarshallers;
    this.prepareStatement = preparedStatementCache;
    this.queryResultExchange = queryResultExchange;
  }

  public final Exchange<Fetch> apply(final String query, final List<Value<?>> params) {
    return prepareStatement.apply(query, params)
        .flatMap(id -> Exchange.send(marshallers.bind, new Bind(id, id, binary, params, binary))
            .thenSend(marshallers.describe, new Describe.DescribePortal(id))
            .map(desc -> new Fetch(id)));
  }

}
