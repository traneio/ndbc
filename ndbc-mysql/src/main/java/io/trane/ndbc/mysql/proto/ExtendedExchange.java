package io.trane.ndbc.mysql.proto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.trane.ndbc.mysql.proto.Message.ExecuteStatementCommand;
import io.trane.ndbc.mysql.proto.Message.OkPrepareStatement;
import io.trane.ndbc.mysql.proto.Message.PrepareStatementCommand;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.util.PartialFunction;
import io.trane.ndbc.value.Value;

public final class ExtendedExchange {

	private final Map<String, Long> prepared = new HashMap<>();

	private final Exchange<Long> readStatementId = Exchange
			.receive(PartialFunction.when(OkPrepareStatement.class, msg -> Exchange.value(msg.statementId)));

	public final <T> Exchange<T> apply(final String query, final List<Value<?>> params, final Exchange<T> readResult) {
		return withParsing(query, params, id -> Exchange.send(new ExecuteStatementCommand(id)).then(readResult));
	}

	private final <T> Exchange<T> withParsing(final String query, final List<Value<?>> params,
			final Function<Long, Exchange<T>> f) {
		String positionalQuery = positional(query);
		Long statementId = prepared.get(positionalQuery);
		if (statementId != null)
			return f.apply(statementId);
		else
			return Exchange.send(new PrepareStatementCommand(query)).then(readStatementId)
					.onSuccess(id -> Exchange.value(prepared.put(positionalQuery, statementId))).flatMap(f::apply);
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
