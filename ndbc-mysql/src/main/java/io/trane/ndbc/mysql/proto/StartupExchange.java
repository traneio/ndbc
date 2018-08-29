package io.trane.ndbc.mysql.proto;

import java.util.Optional;
import java.util.function.Function;

import io.trane.ndbc.mysql.proto.Message.Handshake;
import io.trane.ndbc.mysql.proto.Message.HandshakeResponseMessage;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.mysql.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;

public class StartupExchange {

  private final Exchange<Long> getConnectionIdExchange;
  private final Marshallers    marshallers;
  private final Unmarshallers  unmarshallers;
  private final Exchange<Void> okPacketVoid;

  public StartupExchange(SimpleQueryExchange simpleQueryExchange, Marshallers marshallers,
      Unmarshallers unmarshallers, Exchange<Void> okPacketVoid) {
    this.marshallers = marshallers;
    this.unmarshallers = unmarshallers;
    this.okPacketVoid = okPacketVoid;
    this.getConnectionIdExchange = simpleQueryExchange.apply("SELECT CONNECTION_ID()")
        .map(rows -> rows.get(0).column(0).getLong());
  }

  public Exchange<Long> apply(final String username, final Optional<String> password,
      final Optional<String> database, final String encoding) {
    return Exchange
        .receive(unmarshallers.handshake)
        .flatMap(msg -> doHandshake(username, password, database, encoding).apply(msg))
        .flatMap(v -> getConnectionIdExchange).onFailure(ex -> Exchange.CLOSE);
  }

  private Function<Handshake, Exchange<Void>> doHandshake(final String username,
      final Optional<String> password, final Optional<String> database, final String encoding) {
    return msg -> Exchange
        .send(marshallers.handshakeResponsePacket,
            handshakeResponse(msg.sequence + 1, username, password, database, encoding, msg.seed))
        .thenWaitFor(okPacketVoid);
  }

  private HandshakeResponseMessage handshakeResponse(final int sequence, final String username,
      final Optional<String> password, final Optional<String> database, final String encoding,
      final byte[] seed) {
    return new HandshakeResponseMessage(sequence, username, password, database, encoding, seed,
        "mysql_native_password");
  }
}
