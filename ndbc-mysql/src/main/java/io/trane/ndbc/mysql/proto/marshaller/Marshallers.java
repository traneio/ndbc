package io.trane.ndbc.mysql.proto.marshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.encoding.EncodingRegistry;

public class Marshallers {

  public final CloseStatementCommandMarshaller   closeStatementCommand;
  public final ExecuteStatementCommandMarshaller executeStatementCommand;
  public final HandshakeResponsePacketMarshaller handshakeResponsePacket;
  public final TextCommandMarshaller             textCommand;

  public Marshallers(final EncodingRegistry encoding, final Charset charset) {
    this.closeStatementCommand = new CloseStatementCommandMarshaller();
    this.executeStatementCommand = new ExecuteStatementCommandMarshaller(encoding, charset);
    this.handshakeResponsePacket = new HandshakeResponsePacketMarshaller();
    this.textCommand = new TextCommandMarshaller();
  }
}
