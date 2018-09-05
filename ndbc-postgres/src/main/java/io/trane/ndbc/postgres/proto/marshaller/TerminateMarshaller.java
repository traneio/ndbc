package io.trane.ndbc.postgres.proto.marshaller;

import io.trane.ndbc.postgres.proto.Message.Terminate;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;

public final class TerminateMarshaller implements Marshaller<Terminate> {

  @Override
  public final void apply(final Terminate msg, final BufferWriter b) {
    b.writeChar('X');
    b.writeInt(0);
    b.writeLength(1);
  }
}
