package io.trane.ndbc.mysql.proto;

import java.util.Optional;
import java.util.function.Function;

import io.trane.ndbc.mysql.proto.Message.ErrorResponseMessage;
import io.trane.ndbc.mysql.proto.Message.HandshakeResponseMessage;
import io.trane.ndbc.mysql.proto.Message.InitialHandshakeMessage;
import io.trane.ndbc.mysql.proto.Message.OkResponseMessage;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.PartialFunction;

public class StartupExchange {

	public Exchange<Void> apply(final String username, final Optional<String> password, final Optional<String> database,
			final String encoding) {
		return Exchange.receive(PartialFunction.when(InitialHandshakeMessage.class,
				doHandshake(username, password, database, encoding))).onFailure(ex -> Exchange.CLOSE);
	}

	private Function<InitialHandshakeMessage, Exchange<Void>> doHandshake(final String username, final Optional<String> password,
			final Optional<String> database, final String encoding) {
		return msg -> Exchange
				.send(handshakeResponse(msg.sequence + 1, username, password, database, encoding, msg.seed))
				.thenReceive(okResponse.orElse(errorResponse));
	}

	private final PartialFunction<ServerMessage, Exchange<Void>> okResponse = PartialFunction.when(OkResponseMessage.class,
			msg -> Exchange.VOID);

	private final PartialFunction<ServerMessage, Exchange<Void>> errorResponse = PartialFunction.when(
			ErrorResponseMessage.class,
			errorMessage -> Exchange.fail(String.format("Fail to connect errorMessage=%s", errorMessage.errorMessage)));

	private HandshakeResponseMessage handshakeResponse(final int sequence, final String username, final Optional<String> password,
			final Optional<String> database, final String encoding, final byte[] seed) {
		return new HandshakeResponseMessage(sequence, username, password, database, encoding, seed,
				"mysql_native_password");
	}

}
