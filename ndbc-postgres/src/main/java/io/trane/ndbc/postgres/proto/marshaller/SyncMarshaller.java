package io.trane.ndbc.postgres.proto.marshaller;

import io.trane.ndbc.postgres.proto.Message.Sync;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;

public final class SyncMarshaller implements Marshaller<Sync> {

  @Override
  public final void apply(final Sync msg, final BufferWriter b) {
    b.writeChar('S');
    b.writeInt(4);
  }
}
