package io.trane.ndbc.mysql.proto.marshaller;

import static io.trane.ndbc.mysql.proto.Message.*;
import io.trane.ndbc.mysql.proto.Message;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.ClientMessage;

import java.nio.charset.Charset;
import java.util.logging.Logger;


public class Marshaller {
  private static final Logger log = Logger.getLogger(Marshaller.class.getName());
  private HandshakeResponsePacketMarshaller handshakeResponsePacketMarshaller = new HandshakeResponsePacketMarshaller();
  private TextCommandMarshaller textCommandMarshaller = new TextCommandMarshaller();
  private Charset charset = Charset.forName("UTF-8"); // TODO: Move to config

  public void encode(Message msg, BufferWriter bw) {
    if(msg instanceof HandshakeResponseMessage) {
      handshakeResponsePacketMarshaller.encode((HandshakeResponseMessage) msg, bw, charset);
    } else if(msg instanceof TextCommand) {
      textCommandMarshaller.encode((TextCommand) msg, bw, charset);
    } else {
      log.severe("Invalid client message: " + msg);
    }
  }
}
