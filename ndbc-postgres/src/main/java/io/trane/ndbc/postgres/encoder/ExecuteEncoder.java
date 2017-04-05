package io.trane.ndbc.postgres.encoder;

import io.trane.ndbc.postgres.proto.Message.Execute;
import io.trane.ndbc.proto.BufferWriter;

public class ExecuteEncoder {

  public final void encode(Execute msg, BufferWriter b) {
    b.writeChar('E');
    b.writeCString(msg.portalName);
    b.writeInt(msg.maxNumberOfRows);
    b.writeLength(1);
  }
}
