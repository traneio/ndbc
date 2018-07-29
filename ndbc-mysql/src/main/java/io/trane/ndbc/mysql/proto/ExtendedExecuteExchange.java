package io.trane.ndbc.mysql.proto;

import java.util.List;
import java.util.function.BiFunction;

import io.trane.ndbc.mysql.proto.Message.CloseStatementCommand;
import io.trane.ndbc.mysql.proto.Message.ExecuteStatementCommand;
import io.trane.ndbc.mysql.proto.Message.OkPrepareStatement;
import io.trane.ndbc.mysql.proto.Message.OkResponseMessage;
import io.trane.ndbc.mysql.proto.Message.PrepareStatementCommand;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.PartialFunction;
import io.trane.ndbc.value.Value;

public final class ExtendedExecuteExchange implements BiFunction<String, List<Value<?>>, Exchange<Long>> {

	@Override
	public Exchange<Long> apply(final String command, final List<Value<?>> params) {
		return Exchange.send(new PrepareStatementCommand(command)).thenReceive(PartialFunction.when(
				OkPrepareStatement.class,
				msg -> Exchange.send(new ExecuteStatementCommand(msg.statementId)).thenReceive(commandComplete(msg))));
	}

	private PartialFunction<ServerMessage, Exchange<Long>> commandComplete(final OkPrepareStatement ps) {
		return PartialFunction.when(OkResponseMessage.class,
				msg -> Exchange.send(new CloseStatementCommand(ps.statementId)).then(Exchange.value(msg.affectedRows)));
	}
}
