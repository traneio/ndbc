package io.trane.ndbc.postgres.proto.serializer;

import io.trane.ndbc.postgres.proto.Message.Parse;
import io.trane.ndbc.proto.BufferWriter;

public final class ParseSerializer {

  public final void encode(final Parse msg, final BufferWriter b) {
    b.writeChar('P');
    b.writeInt(0);

    b.writeCString(msg.destinationName);
    b.writeCString(msg.query);
    b.writeShort((short) msg.parameterTypes.length);

    for (final int p : msg.parameterTypes)
      b.writeInt(p);

    b.writeLength(1);
  }
}
