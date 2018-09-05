package io.trane.ndbc.postgres.proto.marshaller;

import io.trane.ndbc.postgres.proto.Message.Query;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;

public final class QueryMarshaller implements Marshaller<Query> {

  @Override
  public final void apply(final Query msg, final BufferWriter b) {
    b.writeChar('Q');
    b.writeInt(0);
    b.writeCString(msg.string);
    b.writeLength(1);
  }
}
