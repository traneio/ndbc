package io.trane.ndbc.postgres.proto.serializer;

import io.trane.ndbc.postgres.proto.Message.Flush;
import io.trane.ndbc.proto.BufferWriter;

public final class SyncSerializer {

  public final void encode(final Flush msg, final BufferWriter b) {
    b.writeChar('S');
    b.writeInt(4);
  }
}
