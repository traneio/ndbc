package io.trane.ndbc.postgres.proto.serializer;

import io.trane.ndbc.postgres.proto.Message.Query;
import io.trane.ndbc.proto.BufferWriter;

public final class QuerySerializer {

  public final void encode(final Query msg, final BufferWriter b) {
    b.writeChar('Q');
    b.writeInt(0);
    b.writeCString(msg.string);
    b.writeLength(1);
  }
}
