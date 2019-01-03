package io.trane.ndbc.postgres.proto;

import java.util.List;

import io.trane.ndbc.postgres.encoding.Format;
import io.trane.ndbc.postgres.proto.Message.Bind;
import io.trane.ndbc.postgres.proto.Message.Close;
import io.trane.ndbc.postgres.proto.Message.Describe;
import io.trane.ndbc.postgres.proto.Message.Execute;
import io.trane.ndbc.postgres.proto.Message.Sync;
import io.trane.ndbc.postgres.proto.marshaller.Marshallers;
import io.trane.ndbc.postgres.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedExchange {

  private final short[]                  binary = { Format.BINARY.getCode() };
  private final Sync                     sync   = new Sync();
  private final Marshallers              marshallers;
  private final Unmarshallers            unmarshallers;
  private final PrepareStatementExchange preparedStatementCache;

  public ExtendedExchange(final Marshallers marshallers, final Unmarshallers unmarshallers,
      final PrepareStatementExchange preparedStatementCache) {
    this.marshallers = marshallers;
    this.unmarshallers = unmarshallers;
    this.preparedStatementCache = preparedStatementCache;
  }

  public final <T> Exchange<T> apply(final String query, final List<Value<?>> params, final Exchange<T> readResult) {
    return preparedStatementCache.apply(query, params)
        .flatMap(id -> Exchange.send(marshallers.bind, new Bind(id, id, binary, params, binary))
            .thenSend(marshallers.describe, new Describe.DescribePortal(id))
            .thenSend(marshallers.execute, new Execute(id, 0))
            .thenSend(marshallers.close, new Close.ClosePortal(id))
            .thenSend(marshallers.sync, sync)
            .thenReceive(unmarshallers.bindComplete)
            .then(readResult)
            .thenReceive(unmarshallers.closeComplete)
            .thenWaitFor(unmarshallers.readyForQuery));
  }

}
