package io.trane.ndbc.postgres.proto.serializer;

import io.trane.ndbc.postgres.proto.Message.Flush;
import io.trane.ndbc.proto.BufferWriter;

public class FlushSerializer {

  public final void encode(Flush msg, BufferWriter b) {
    b.writeChar('F');
    b.writeLength(1);
  }
}
