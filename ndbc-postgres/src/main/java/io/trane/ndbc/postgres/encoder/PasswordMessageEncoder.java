package io.trane.ndbc.postgres.encoder;

import io.trane.ndbc.postgres.proto.Message.PasswordMessage;
import io.trane.ndbc.proto.BufferWriter;

public class PasswordMessageEncoder {

  public final void encode(PasswordMessage msg, BufferWriter b) {
    b.writeChar('p');
    b.writeInt(0);
    b.writeCString(msg.password);
    b.writeLength(1);
  }
}
