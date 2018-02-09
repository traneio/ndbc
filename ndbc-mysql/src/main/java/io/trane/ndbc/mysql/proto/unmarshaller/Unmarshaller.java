package io.trane.ndbc.mysql.proto.unmarshaller;

import java.util.Optional;

import io.trane.ndbc.mysql.proto.Message;
import io.trane.ndbc.mysql.proto.Message.*;
import io.trane.ndbc.proto.BufferReader;

public class Unmarshaller {
    private final InitialHandshakePacketUnmarshaller initialHandshakePacketUnmarshaller = new InitialHandshakePacketUnmarshaller();
    private final ServerResponseUnmarshaller serverResponseUnmarshaller = new ServerResponseUnmarshaller();
    private final TextResultSetUnmarshaller textResultSetUnmarshaller = new TextResultSetUnmarshaller();

    public final Optional<Message> decode(final ClientMessage previousClientMessage, final BufferReader b) throws Exception {
        if(previousClientMessage instanceof NoCommand) {
          return Optional.of(initialHandshakePacketUnmarshaller.decode(b));
        } if(previousClientMessage instanceof HandshakeResponseMessage)  {
          return Optional.of(serverResponseUnmarshaller.decode(b));
        } if(previousClientMessage instanceof QueryCommand) {
          return Optional.of(textResultSetUnmarshaller.decode(b));
        } if(previousClientMessage instanceof StatementCommand) {
          return Optional.of(serverResponseUnmarshaller.decode(b));
        } else {
          throw new IllegalStateException("Unknown message");
        }
    }
}
