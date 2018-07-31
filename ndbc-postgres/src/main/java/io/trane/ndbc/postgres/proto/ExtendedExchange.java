package io.trane.ndbc.postgres.proto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import io.trane.ndbc.postgres.encoding.Format;
import io.trane.ndbc.postgres.proto.Message.Bind;
import io.trane.ndbc.postgres.proto.Message.BindComplete;
import io.trane.ndbc.postgres.proto.Message.Close;
import io.trane.ndbc.postgres.proto.Message.CloseComplete;
import io.trane.ndbc.postgres.proto.Message.Describe;
import io.trane.ndbc.postgres.proto.Message.Execute;
import io.trane.ndbc.postgres.proto.Message.Parse;
import io.trane.ndbc.postgres.proto.Message.ParseComplete;
import io.trane.ndbc.postgres.proto.Message.ReadyForQuery;
import io.trane.ndbc.postgres.proto.Message.Sync;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedExchange {

	private final short[] binary = {Format.BINARY.getCode()};
	private final Sync sync = new Sync();
	private final Set<Integer> prepared = new HashSet<>();

	public final <T> Exchange<T> apply(final String query, final List<Value<?>> params, final Exchange<T> readResult) {
		return withParsing(query, params,
				id -> Exchange.send(new Bind(id, id, binary, params, binary)).thenSend(new Describe.DescribePortal(id))
						.thenSend(new Execute(id, 0)).thenSend(new Close.ClosePortal(id)).thenSend(sync))
								.thenReceive(BindComplete.class).then(readResult).thenReceive(CloseComplete.class)
								.thenWaitFor(ReadyForQuery.class);
	}

	private final <T> Exchange<T> withParsing(final String query, final List<Value<?>> params,
			final Function<String, Exchange<T>> f) {
		final int id = id(query, params);
		final String idString = Integer.toString(id);
		if (prepared.contains(id))
			return f.apply(idString);
		else
			return Exchange.send(new Parse(Integer.toString(id), positional(query), params)).then(f.apply(idString))
					.thenReceive(ParseComplete.class).onSuccess(ign -> Exchange.value(prepared.add(id)));
	}

	private final int id(final String query, final List<Value<?>> params) {
		int id = query.hashCode();
		for (final Value<?> v : params)
			id = 31 * id + v.getClass().hashCode();
		return id;
	}

	// TODO handle quotes, comments, etc.
	private final String positional(final String query) {
		int idx = 0;
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < query.length(); i++) {
			final char c = query.charAt(i);
			if (c == '?') {
				idx += 1;
				sb.append("$");
				sb.append(idx);
			} else
				sb.append(c);
		}
		return sb.toString();
	}
}
