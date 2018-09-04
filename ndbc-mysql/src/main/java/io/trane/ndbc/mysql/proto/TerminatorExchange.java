package io.trane.ndbc.mysql.proto;

import io.trane.ndbc.mysql.proto.Message.EofPacket;
import io.trane.ndbc.mysql.proto.Message.OkPacket;
import io.trane.ndbc.mysql.proto.Message.PrepareOk;
import io.trane.ndbc.mysql.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;

public class TerminatorExchange {

	public final Exchange<Void> okPacketVoid;
	public final Exchange<Long> affectedRows;
	public final Exchange<PrepareOk> prepareOk;

	public TerminatorExchange(Unmarshallers unmarshallers) {
		this.okPacketVoid = Exchange.receive(unmarshallers.terminator).flatMap(msg -> {
			if (msg instanceof OkPacket || msg instanceof EofPacket)
				return Exchange.VOID;
			else
				return Exchange.fail(msg.toString());
		});
		this.affectedRows = Exchange.receive(unmarshallers.terminator).flatMap(msg -> {
			if (msg instanceof OkPacket)
				return Exchange.value(((OkPacket) msg).affectedRows);
			else if (msg instanceof EofPacket)
				return Exchange.value(0L);
			else
				return Exchange.fail(msg.toString());
		});
		this.prepareOk = Exchange.receive(unmarshallers.prepareOk.orElse(unmarshallers.terminator)).flatMap(msg -> {
			if (msg instanceof PrepareOk)
				return Exchange.value((PrepareOk) msg);
			else
				return Exchange.fail(msg.toString());
		});
	}
}
