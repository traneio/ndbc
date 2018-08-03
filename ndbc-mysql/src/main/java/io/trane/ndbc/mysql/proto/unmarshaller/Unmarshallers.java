package io.trane.ndbc.mysql.proto.unmarshaller;

public class Unmarshallers {

  public final BinaryResultSetUnmarshaller        binaryResultSet        = new BinaryResultSetUnmarshaller();
  public final ColumnCountUnmarshaller            columnCount            = new ColumnCountUnmarshaller();
  public final InitialHandshakePacketUnmarshaller initialHandshakePacket = new InitialHandshakePacketUnmarshaller();
  public final PrepareStatementOkUnmarshaller     prepareStatementOk     = new PrepareStatementOkUnmarshaller();
  public final ServerResponseUnmarshaller         serverResponse         = new ServerResponseUnmarshaller();
  public final TextResultSetUnmarshaller          textResultSet          = new TextResultSetUnmarshaller();
}
