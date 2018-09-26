package io.trane.ndbc.sqlserver.proto;

import java.util.Optional;

import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.sqlserver.proto.Message.PreLogin;
import io.trane.ndbc.sqlserver.proto.marshaller.Marshallers;
import io.trane.ndbc.sqlserver.proto.unmarshaller.Unmarshallers;

public final class StartupExchange {

  private final Marshallers marshallers;
  private final Unmarshallers unmarshallers;

  public StartupExchange(final Marshallers marshallers, final Unmarshallers unmarshallers) {
    this.marshallers = marshallers;
    this.unmarshallers = unmarshallers;
  }

  public Exchange<Long> apply(final String username, final Optional<String> password, final Optional<String> database,
      final String encoding) {
    return Exchange.send(marshallers.preLogin, new PreLogin()).then(Exchange.receive(unmarshallers.preLoginResponse))
        .flatMap(x -> Exchange.value(x.id)).onFailure(ex -> Exchange.CLOSE);
  }
}
