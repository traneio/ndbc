package io.trane.ndbc.postgres.proto.marshaller;

import io.trane.ndbc.postgres.proto.Message.Sync;
import io.trane.ndbc.proto.BufferWriter;

public final class SyncMarshaller {

  public final void encode(final Sync msg, final BufferWriter b) {
    b.writeChar('S');
    b.writeInt(4);
  }
}
