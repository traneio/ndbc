package io.trane.ndbc.postgres.proto.serializer;

import io.trane.ndbc.postgres.proto.Message.Execute;
import io.trane.ndbc.proto.BufferWriter;

public class ExecuteSerializer {

  public final void encode(Execute msg, BufferWriter b) {
    b.writeChar('E');
    b.writeCString(msg.portalName);
    b.writeInt(msg.maxNumberOfRows);
    b.writeLength(1);
  }
}
