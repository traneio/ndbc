package io.trane.ndbc.postgres.proto;

import java.util.Optional;

import io.trane.ndbc.Config;
import io.trane.ndbc.Config.SSL.Mode;
import io.trane.ndbc.postgres.proto.Message.SSLRequest;
import io.trane.ndbc.postgres.proto.Message.SSLResponse;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.util.PartialFunction;

public class InitSSLExchange {

  private final Exchange<Optional<Config.SSL>> disabled   = Exchange.value(Optional.empty());
  private final SSLRequest                     sslRequest = new SSLRequest();

  public final Exchange<Optional<Config.SSL>> apply(final Optional<Config.SSL> optCfg) {
    return optCfg.map(cfg -> {
      if (cfg.mode() == Config.SSL.Mode.DISABLE)
        return disabled;
      else
        return Exchange.send(sslRequest).thenReceive(PartialFunction.when(SSLResponse.class, r -> {
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