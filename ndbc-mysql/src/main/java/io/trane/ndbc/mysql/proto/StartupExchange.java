package io.trane.ndbc.mysql.proto;

import java.util.Optional;
import java.util.function.Function;

import io.trane.ndbc.mysql.proto.Message.HandshakeResponseMessage;
import io.trane.ndbc.mysql.proto.Message.InitialHandshakeMessage;
import io.trane.ndbc.mysql.proto.Message.OkResponseMessage;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.mysql.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;

public class StartupExchange {

  private final Exchange<String> getConnectionIdExchange;
  private final Marshallers      marshallers;
  private final Unmarshallers    unmarshallers;

  public StartupExchange(SimpleQueryExchange simpleQueryExchange, Marshallers marshallers,
      Unmarshallers unmarshallers) {
    this.marshallers = marshallers;
    this.unmarshallers = unmarshallers;
    this.getConnectionIdExchange = simpleQueryExchange.apply("SELECT CONNECTION_ID()")
        .map(rows -> rows.get(0).column(0).getString());
  }

  public Exchange<String> apply(final String username, final Optional<String> password,
      final Optional<String> database, final String encoding) {
    return Exchange
        .receive(unmarshallers.initialHandshakePacket)
        .flatMap(msg -> doHandshake(username, password, database, encoding).apply(msg))
        .flatMap(v -> getConnectionIdExchange).onFailure(ex -> Exchange.CLOSE);
  }

  private Function<InitialHandshakeMessage, Exchange<Void>> doHandshake(final String username,
      final Optional<String> password, final Optional<String> database, final String encoding) {
    return msg -> Exchange
        .send(marshallers.handshakeResponsePacket,
            handshakeResponse(msg.sequence + 1, username, password, database, encoding, msg.seed))
        .then(Exchange.receive(unmarshallers.serverResponse).flatMap(r -> {
          if (r instanceof OkResponseMessage)
            return Exchange.VOID;
          else
            return Exchange.fail(r.toString());
        }));
  }

  private HandshakeResponseMessage handshakeResponse(final int sequence, final String username,
      final Optional<String> password, final Optional<String> database, final String encoding,
      final byte[] seed) {
    return new HandshakeResponseMessage(sequence, username, password, database, encoding, seed,
        "mysql_native_password");
  }

}
