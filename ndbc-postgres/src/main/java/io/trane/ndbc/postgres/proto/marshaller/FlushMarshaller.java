package io.trane.ndbc.postgres.proto.marshaller;

import io.trane.ndbc.postgres.proto.Message.Flush;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;

public final class FlushMarshaller implements Marshaller<Flush> {

  public final void apply(final Flush msg, final BufferWriter b) {
    b.writeChar('H');
    b.writeInt(4);
  }
}
