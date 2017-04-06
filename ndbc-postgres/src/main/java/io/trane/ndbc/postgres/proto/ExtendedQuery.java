package io.trane.ndbc.postgres.proto;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.ResultSet;
import io.trane.ndbc.postgres.proto.Message.InfoResponse.ErrorResponse;
import io.trane.ndbc.postgres.proto.Message.Parse;
import io.trane.ndbc.postgres.proto.Message.ParseComplete;
import io.trane.ndbc.postgres.proto.Message.Bind;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.PartialFunction;

public class ExtendedQuery {

  public Exchange<ResultSet> apply(PreparedStatement ps) {
    return prepare(ps.getQuery()).flatMap(id -> {
      return Exchange.receive(PartialFunction.<ServerMessage, Exchange<ResultSet>>apply()
          .orElse(ErrorResponse.class, msg -> Exchange.fail(msg.toString()))
          .orElse(ParseComplete.class, msg -> {
//            Exchange.send(new Bind("", id, parameterFormatCodes, fields, resultColumnFormatCodes));
            return null;
          }));
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
          .then(Exchange.value(idString));
  }
}
