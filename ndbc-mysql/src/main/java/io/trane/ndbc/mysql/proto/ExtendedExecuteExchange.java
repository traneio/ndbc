package io.trane.ndbc.mysql.proto;

import java.util.List;
import java.util.function.BiFunction;

import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedExecuteExchange implements BiFunction<String, List<Value<?>>, Exchange<Long>> {

	private final ExtendedExchange extendedExchange;
	private final Exchange<Long> affectedRows;

	public ExtendedExecuteExchange(ExtendedExchange extendedExchange, final Exchange<Long> affectedRows) {
		this.extendedExchange = extendedExchange;
		this.affectedRows = affectedRows;
	}

	@Override
	public Exchange<Long> apply(final String command, final List<Value<?>> params) {
		return extendedExchange.apply(command, params, affectedRows);
	}
}
