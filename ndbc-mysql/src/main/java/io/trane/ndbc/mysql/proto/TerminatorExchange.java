package io.trane.ndbc.mysql.proto;

import io.trane.ndbc.mysql.proto.Message.OkPacket;
import io.trane.ndbc.mysql.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;

public class TerminatorExchange {

  public final Exchange<OkPacket> okPacket;
  public final Exchange<Void>     okPacketVoid;
  public final Exchange<Long>     affectedRows;

  public TerminatorExchange(Unmarshallers unmarshallers) {
    this.okPacket = Exchange.receive(unmarshallers.terminator)
        .flatMap(msg -> {
          if (msg instanceof OkPacket)
            return Exchange.value(((OkPacket) msg));
          else
            return Exchange.fail(msg.toString());
        });
    this.okPacketVoid = okPacket.flatMap(ok -> Exchange.VOID);
    this.affectedRows = okPacket.map(msg -> msg.affectedRows);
  }
}
