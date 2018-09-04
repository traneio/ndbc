package io.trane.ndbc.postgres.proto;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest;
import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest.AuthenticationCleartextPassword;
import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest.AuthenticationMD5Password;
import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest.AuthenticationOk;
import io.trane.ndbc.postgres.proto.Message.BackendKeyData;
import io.trane.ndbc.postgres.proto.Message.ParameterStatus;
import io.trane.ndbc.postgres.proto.Message.PasswordMessage;
import io.trane.ndbc.postgres.proto.Message.ReadyForQuery;
import io.trane.ndbc.postgres.proto.Message.StartupMessage;
import io.trane.ndbc.postgres.proto.marshaller.Marshallers;
import io.trane.ndbc.postgres.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.postgres.util.MD5Digest;
import io.trane.ndbc.proto.Exchange;

public final class StartupExchange {

	private final Marshallers marshallers;
	private final Unmarshallers unmarshallers;

	public StartupExchange(Marshallers marshallers, Unmarshallers unmarshallers) {
		this.marshallers = marshallers;
		this.unmarshallers = unmarshallers;
	}

	public final Exchange<Optional<BackendKeyData>> apply(final Charset charset, final String user,
			final Optional<String> password, final Optional<String> database) {
		return Exchange.send(marshallers.startupMessage, startupMessage(charset, user, database))
				.then(authenticate(charset, user, password, database)).then(waitForBackendStartup(Optional.empty()));
	}

	private final Exchange<Void> authenticate(final Charset charset, final String user, final Optional<String> password,
			final Optional<String> database) {
		return Exchange.receive(unmarshallers.authenticationRequest).flatMap(r -> {
			if (r instanceof AuthenticationOk)
				return Exchange.VOID;
			else {
				return passwordMessage(charset, user, password, r)
						.flatMap(msg -> Exchange.send(marshallers.passwordMessage, msg)
								.then(Exchange.receive(unmarshallers.authenticationRequest).flatMap(ok -> {
									if (!(ok instanceof AuthenticationOk))
										return Exchange.fail("Authentication failed");
									else
										return Exchange.VOID;
								})));
			}
		});
	}

	private final Exchange<PasswordMessage> passwordMessage(final Charset charset, final String user,
			Optional<String> password, AuthenticationRequest r) {
		return password.<Exchange<PasswordMessage>>map(p -> {
			if (r instanceof AuthenticationCleartextPassword)
				return Exchange.value(new PasswordMessage(p));
			else if (r instanceof AuthenticationMD5Password)
				return Exchange.value(md5PasswordMessage(charset, user, p, ((AuthenticationMD5Password) r).salt));
			else
				return Exchange.CLOSE.thenFail("Database authentication method not supported by ndbc: " + r);
		}).orElse(Exchange.fail("Database requires a password but the configuration doesn't specify one."));
	}

	private final Exchange<Optional<BackendKeyData>> waitForBackendStartup(
			final Optional<BackendKeyData> backendKeyData) {
		return Exchange.receive(
				unmarshallers.backendKeyData.orElse(unmarshallers.parameterStatus).orElse(unmarshallers.readyForQuery))
				.flatMap(msg -> {
					if (msg instanceof BackendKeyData)
						return waitForBackendStartup(Optional.of((BackendKeyData) msg));
					else if (msg instanceof ParameterStatus)
						return waitForBackendStartup(backendKeyData);
					else if (msg instanceof ReadyForQuery)
						return Exchange.value(backendKeyData);
					else
						return Exchange.fail("Unexpected server message " + msg);
				});
	}

	private final PasswordMessage md5PasswordMessage(final Charset charset, final String user, final String password,
			final byte[] salt) {
		final byte[] bytes = MD5Digest.encode(user.getBytes(charset), password.getBytes(charset), salt);
		return new PasswordMessage(new String(bytes, charset));
	}

	private final StartupMessage startupMessage(final Charset charset, final String user,
			final Optional<String> database) {
		final List<StartupMessage.Parameter> params = new ArrayList<>();
		database.ifPresent(db -> params.add(new StartupMessage.Parameter("database", db)));
		params.add(new StartupMessage.Parameter("client_encoding", charset.name()));
		params.add(new StartupMessage.Parameter("DateStyle", "ISO"));
		params.add(new StartupMessage.Parameter("extra_float_digits", "2"));
		return new StartupMessage(user, params.toArray(new StartupMessage.Parameter[0]));
	}
}
