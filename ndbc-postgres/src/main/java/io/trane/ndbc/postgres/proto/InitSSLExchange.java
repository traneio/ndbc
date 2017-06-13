package io.trane.ndbc.postgres.proto;

import java.util.Optional;
import java.util.function.Function;

import io.trane.ndbc.Config;
import io.trane.ndbc.Config.SSL;
import io.trane.ndbc.Config.SSL.Mode;
import io.trane.ndbc.postgres.proto.Message.SSLRequest;
import io.trane.ndbc.postgres.proto.Message.SSLResponse;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.util.PartialFunction;

public class InitSSLExchange<SSLEngine> {

  private final Exchange<Optional<SSLEngine>>   disabled   = Exchange.value(Optional.empty());
  private final SSLRequest                      sslRequest = new SSLRequest();
  private final Function<Config.SSL, SSLEngine> createSSLEngine;

  public InitSSLExchange(Function<SSL, SSLEngine> createSSLEngine) {
    super();
    this.createSSLEngine = createSSLEngine;
  }

  public final Exchange<Optional<SSLEngine>> apply(final Config.SSL cfg) {
    if (cfg.mode() == Config.SSL.Mode.DISABLE)
      return disabled;
    else
      return Exchange.send(sslRequest).thenReceive(PartialFunction.when(SSLResponse.class, r -> {
        if (r.enabled)
          return Exchange.value(Optional.of(createSSLEngine.apply(cfg)));
        else if (cfg.mode() != Mode.PREFER)
          return Exchange.fail("Database doesn't accept SSL connections.");
        else
          return disabled;
      }));
  }
}