package io.trane.ndbc.postgres.encoder;

import io.trane.ndbc.postgres.proto.Message.Flush;
import io.trane.ndbc.proto.BufferWriter;

public class SyncEncoder {

  public final void encode(Flush msg, BufferWriter b) {
    b.writeChar('S');
    b.writeLength(1);
  }
}
