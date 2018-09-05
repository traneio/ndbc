package io.trane.ndbc.postgres.proto;

import java.util.Optional;

import io.trane.ndbc.Config;
import io.trane.ndbc.Config.SSL.Mode;
import io.trane.ndbc.postgres.proto.Message.SSLRequest;
import io.trane.ndbc.postgres.proto.marshaller.Marshallers;
import io.trane.ndbc.postgres.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;

public class InitSSLExchange {

  private static final Exchange<Optional<Config.SSL>> disabled   = Exchange.value(Optional.empty());
  private static final SSLRequest                     sslRequest = new SSLRequest();

  private final Marshallers   marshallers;
  private final Unmarshallers unmarshallers;

  public InitSSLExchange(final Marshallers marshallers, final Unmarshallers unmarshallers) {
    super();
    this.marshallers = marshallers;
    this.unmarshallers = unmarshallers;
  }

  public final Exchange<Optional<Config.SSL>> apply(final Optional<Config.SSL> optCfg) {
    return optCfg.map(cfg -> {
      if (cfg.mode() == Config.SSL.Mode.DISABLE)
        return disabled;
      else
        return Exchange.send(marshallers.sslRequest, sslRequest)
            .then(Exchange.receive(unmarshallers.sslResponse).flatMap(r -> {
              if (r.enabled)
                return Exchange.value(Optional.of(cfg));
              else if (cfg.mode() != Mode.PREFER)
                return Exchange.fail("Database doesn't accept SSL connections.");
              else
                return disabled;
            })).onFailure(ex -> Exchange.CLOSE);
    }).orElse(disabled);
  }
}