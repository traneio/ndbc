package io.trane.ndbc.postgres.proto.serializer;

import io.trane.ndbc.postgres.proto.Message.CancelRequest;
import io.trane.ndbc.proto.BufferWriter;

public class CancelRequestSerializer {

  public final void encode(CancelRequest msg, BufferWriter b) {
    b.writeInt(80877102);
    b.writeInt(msg.processId);
    b.writeInt(msg.secretKey);
    b.writeLength(0);
  }

}
