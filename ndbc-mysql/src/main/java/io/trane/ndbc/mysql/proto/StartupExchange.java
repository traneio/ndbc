package io.trane.ndbc.mysql.proto;

import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.PartialFunction;
import static io.trane.ndbc.mysql.proto.Message.*;

import java.util.Optional;
import java.util.function.Function;

public class StartupExchange {

	public Exchange<Void> apply(String username, Optional<String> password, Optional<String> database,
			String encoding) {
		return Exchange.receive(PartialFunction.when(InitialHandshakeMessage.class,
				doHandshake(username, password, database, encoding))).onFailure(ex -> Exchange.CLOSE);
	}

	private Function<InitialHandshakeMessage, Exchange<Void>> doHandshake(String username, Optional<String> password,
			Optional<String> database, String encoding) {
		return msg -> Exchange
				.send(handshakeResponse(msg.sequence + 1, username, password, database, encoding, msg.seed))
				.thenReceive(okResponse.orElse(errorResponse));
	}

	private PartialFunction<ServerMessage, Exchange<Void>> okResponse = PartialFunction.when(OkResponseMessage.class,
			msg -> Exchange.VOID);

	private PartialFunction<ServerMessage, Exchange<Void>> errorResponse = PartialFunction.when(
			ErrorResponseMessage.class,
			errorMessage -> Exchange.fail(String.format("Fail to connect errorMessage=%s", errorMessage.errorMessage)));

	private HandshakeResponseMessage handshakeResponse(int sequence, String username, Optional<String> password,
			Optional<String> database, String encoding, byte[] seed) {
		return new HandshakeResponseMessage(sequence, username, password, database, encoding, seed,
				"mysql_native_password");
	}

}
