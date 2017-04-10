package io.trane.ndbc.postgres.proto;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.postgres.encoding.Format;
import io.trane.ndbc.postgres.proto.Message.Bind;
import io.trane.ndbc.postgres.proto.Message.BindComplete;
import io.trane.ndbc.postgres.proto.Message.Close;
import io.trane.ndbc.postgres.proto.Message.CloseComplete;
import io.trane.ndbc.postgres.proto.Message.Describe;
import io.trane.ndbc.postgres.proto.Message.Execute;
import io.trane.ndbc.postgres.proto.Message.Flush;
import io.trane.ndbc.postgres.proto.Message.Parse;
import io.trane.ndbc.postgres.proto.Message.ParseComplete;
import io.trane.ndbc.postgres.proto.Message.Sync;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public class ExtendedExchange {

  private final short[] binary = { Format.BINARY.getCode() };
  private final Value<?>[] emptyValues = new Value<?>[0];
  private final Sync sync = new Sync();
  private final Flush flush = new Flush();
  private final Set<Integer> prepared = new HashSet<>();
  private final int[] emptyParams = new int[0];

  public final <T> Exchange<T> apply(PreparedStatement ps, Exchange<T> readResult) {
    return withParsing(ps.getQuery(), id -> Exchange
        .send(new Bind(id, id, binary, ps.getValues().toArray(emptyValues), binary))
        .thenSend(new Describe.DescribePortal(id))
        .thenSend(new Execute(id, 0))
        .thenSend(new Close.ClosePortal(id))
        .thenSend(flush)
        .thenSend(sync))
            .thenReceive(BindComplete.class)
            .then(readResult)
            .thenReceive(CloseComplete.class);
  }

  private final <T> Exchange<T> withParsing(String query, Function<String, Exchange<T>> f) {
    int id = query.hashCode();
    String idString = Integer.toString(id);
    if (prepared.contains(id))
      return f.apply(idString);
    else
      return Exchange
          .send(new Parse(Integer.toString(id), query, emptyParams))
          .then(f.apply(idString))
          .thenReceive(ParseComplete.class)
          .onSuccess(ign -> Exchange.value(prepared.add(id)));
  }
}
