package io.trane.ndbc.postgres.proto.marshaller;

import io.trane.ndbc.postgres.proto.Message.Execute;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;

public final class ExecuteMarshaller implements Marshaller<Execute> {

  @Override
  public final void apply(final Execute msg, final BufferWriter b) {
    b.writeChar('E');
    b.writeInt(0);

    b.writeCString(msg.portalName);
    b.writeInt(msg.maxNumberOfRows);
    b.writeLength(1);
  }
}
