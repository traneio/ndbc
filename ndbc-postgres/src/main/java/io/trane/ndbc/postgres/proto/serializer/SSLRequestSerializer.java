package io.trane.ndbc.postgres.proto.serializer;

import io.trane.ndbc.postgres.proto.Message.SSLRequest;
import io.trane.ndbc.proto.BufferWriter;

public final class SSLRequestSerializer {

  public final void encode(final SSLRequest msg, final BufferWriter b) {
    b.writeInt(8);
    b.writeShort((short) 1234);
    b.writeShort((short) 5679);
  }
}
