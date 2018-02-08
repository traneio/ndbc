package io.trane.ndbc.mysql.proto.unmarshaller;

import static io.trane.ndbc.mysql.proto.Message.*;
import io.trane.ndbc.mysql.proto.Message;
import io.trane.ndbc.proto.BufferReader;

import java.util.Optional;

public class Unmarshaller {
    private InitialHandshakePacketUnmarshaller initialHandshakePacketUnmarshaller = new InitialHandshakePacketUnmarshaller();
    private ServerResponseUnmarshaller serverResponseUnmarshaller = new ServerResponseUnmarshaller();
    private TextResultSetUnmarshaller textResultSetUnmarshaller = new TextResultSetUnmarshaller();
    public final Optional<Message> decode(ClientMessage previousClientMessage, final BufferReader b) throws Exception {
        if(previousClientMessage instanceof NoCommand) {
          return Optional.of(initialHandshakePacketUnmarshaller.decode(b));
        } if(previousClientMessage instanceof HandshakeResponseMessage)  {
          return Optional.of(serverResponseUnmarshaller.decode(b));
        } if(previousClientMessage instanceof QueryCommand) {
          return Optional.of(textResultSetUnmarshaller.decode(b));
        } else {
          throw new IllegalStateException("Unknown message");
        }
    }

}
