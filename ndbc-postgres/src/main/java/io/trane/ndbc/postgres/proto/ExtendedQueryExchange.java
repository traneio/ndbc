package io.trane.ndbc.postgres.proto;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.PreparedStatement.Param;
import io.trane.ndbc.ResultSet;
import io.trane.ndbc.postgres.encoding.Format;
import io.trane.ndbc.postgres.proto.Message.Bind;
import io.trane.ndbc.postgres.proto.Message.Parse;
import io.trane.ndbc.postgres.proto.Message.ParseComplete;
import io.trane.ndbc.proto.Exchange;

public class ExtendedQueryExchange {
  
  private final short[] binary = { Format.BINARY.getCode() };

  public Exchange<ResultSet> apply(PreparedStatement ps) {
    return prepare(ps.getQuery()).flatMap(id -> {
      List<Param> params = ps.getParams();
      new Bind(id, id, binary, null, binary);
      return null;
    });
  }

  private final Set<Integer> prepared = Collections.newSetFromMap(new ConcurrentHashMap<>());
  private final int[] emptyParams = new int[0];

  private Exchange<String> prepare(String query) {
    int id = query.hashCode();
    String idString = Integer.toString(id);
    if (prepared.contains(id))
      return Exchange.value(idString);
    else
      return Exchange.send(new Parse(idString, query, emptyParams))
          .thenReceive(ParseComplete.class)
          .map(v -> {
            prepared.add(id);
            return idString;
          });
  }
}
