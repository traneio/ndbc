package io.trane.ndbc.postgres.proto.serializer;

import io.trane.ndbc.postgres.proto.Message.Close;
import io.trane.ndbc.proto.BufferWriter;

public class CloseSerializer {

  public final void encode(Close msg, BufferWriter b) {
    b.writeChar('C');

    if (msg instanceof Close.ClosePreparedStatement)
      b.writeChar('S');
    else if (msg instanceof Close.ClosePortal)
      b.writeChar('P');
    else
      throw new IllegalStateException("Invalid close message: " + msg);

    b.writeCString(msg.name);
    b.writeLength(1);
  }
}
