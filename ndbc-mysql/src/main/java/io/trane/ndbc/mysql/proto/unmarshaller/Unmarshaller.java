package io.trane.ndbc.mysql.proto.unmarshaller;

import io.trane.ndbc.mysql.proto.Message.ClientMessage;
import io.trane.ndbc.mysql.proto.Message.ExecuteStatementCommand;
import io.trane.ndbc.mysql.proto.Message.HandshakeResponseMessage;
import io.trane.ndbc.mysql.proto.Message.NoCommand;
import io.trane.ndbc.mysql.proto.Message.PrepareStatementCommand;
import io.trane.ndbc.mysql.proto.Message.QueryCommand;
import io.trane.ndbc.mysql.proto.Message.StatementCommand;
import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.Try;

public class Unmarshaller {
  private final InitialHandshakePacketUnmarshaller initialHandshakePacketUnmarshaller = new InitialHandshakePacketUnmarshaller();
  private final ServerResponseUnmarshaller serverResponseUnmarshaller = new ServerResponseUnmarshaller();
  private final TextResultSetUnmarshaller textResultSetUnmarshaller = new TextResultSetUnmarshaller();
  private final PrepareStatementOkUnmarshaller prepareStatementOkUnmarshaller = new PrepareStatementOkUnmarshaller();

  public final Try<ServerMessage> decode(final ClientMessage previousClientMessage, final BufferReader b) {
    if (previousClientMessage instanceof NoCommand) {
      return Try.apply(() -> initialHandshakePacketUnmarshaller.decode(b));
    }
    if (previousClientMessage instanceof HandshakeResponseMessage) {
      return Try.apply(() -> serverResponseUnmarshaller.decode(b));
    }
    if (previousClientMessage instanceof QueryCommand) {
      return Try.apply(() -> textResultSetUnmarshaller.decode(b));
    }
    if (previousClientMessage instanceof StatementCommand) {
      return Try.apply(() -> serverResponseUnmarshaller.decode(b));
    }
    if (previousClientMessage instanceof PrepareStatementCommand) {
      return Try.apply(() -> prepareStatementOkUnmarshaller.decode(b));
    }
    if (previousClientMessage instanceof ExecuteStatementCommand) {
      return Try.apply(() -> serverResponseUnmarshaller.decode(b));
    } else {
      return Try.failure(new IllegalStateException("Unknown message"));
    }
  }
}
