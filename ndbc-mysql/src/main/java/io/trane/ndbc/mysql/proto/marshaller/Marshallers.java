package io.trane.ndbc.mysql.proto.marshaller;

public class Marshallers {

  public final CloseStatementCommandMarshaller   closeStatementCommand   = new CloseStatementCommandMarshaller();
  public final ExecuteStatementCommandMarshaller executeStatementCommand = new ExecuteStatementCommandMarshaller();
  public final HandshakeResponsePacketMarshaller handshakeResponsePacket = new HandshakeResponsePacketMarshaller();
  public final TextCommandMarshaller             textCommand             = new TextCommandMarshaller();
}
