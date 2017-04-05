package io.trane.ndbc.postgres.encoder;

import io.trane.ndbc.postgres.proto.Message.Query;
import io.trane.ndbc.proto.BufferWriter;

public class QueryEncoder {

  public final void encode(Query msg, BufferWriter b) {
    b.writeChar('Q');
    b.writeCString(msg.string);
    b.writeLength(1);
  }
}
