package io.trane.ndbc.postgres.proto;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest;
import io.trane.ndbc.postgres.proto.Message.BackendMessage;
import io.trane.ndbc.postgres.proto.Message.ErrorResponse;
import io.trane.ndbc.postgres.proto.Message.PasswordMessage;
import io.trane.ndbc.postgres.proto.Message.StartupMessage;
import io.trane.ndbc.util.PartialFunction;

public class Startup {

  public Exchange<Void> apply(Charset charset, String user, Optional<String> database, Optional<String> password) {
    return Exchange.send(startupMessage(charset, user, database)).thenReceive(
        authenticationOk.orElse(clearTextPasswordAuthentication(password)).orElse(unsupportedAuthentication));
  }

  private final PartialFunction<BackendMessage, Exchange<Void>> authenticationOk = PartialFunction
      .when(AuthenticationRequest.AuthenticationOk.class, msg -> Exchange.done());

  private final PartialFunction<BackendMessage, Exchange<Void>> clearTextPasswordAuthentication(
      Optional<String> password) {
    return PartialFunction.when(AuthenticationRequest.AuthenticationCleartextPassword.class,
        msg -> password.map(p -> Exchange.send(new PasswordMessage(p)).thenReceive(authenticationOk))
            .orElse(Exchange.fail("Database requires a password but the configuration doesn't specify one.")));
  }

  private final PartialFunction<BackendMessage, Exchange<Void>> unsupportedAuthentication = PartialFunction
      .when(AuthenticationRequest.class, msg -> Exchange.fail("Authentication method not supported by ndbc: " + msg));

  private StartupMessage startupMessage(Charset charset, String user, Optional<String> database) {
    List<StartupMessage.Parameter> params = new ArrayList<>();
    database.ifPresent(db -> params.add(new StartupMessage.Parameter("database", db)));
    params.add(new StartupMessage.Parameter("client_encoding", charset.name()));
    params.add(new StartupMessage.Parameter("DateStyle", "ISO"));
    params.add(new StartupMessage.Parameter("extra_float_digits", "2"));
    return new StartupMessage(user, params.toArray(new StartupMessage.Parameter[0]));
  }
}
