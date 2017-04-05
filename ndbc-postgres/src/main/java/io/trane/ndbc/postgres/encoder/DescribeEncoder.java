package io.trane.ndbc.postgres.encoder;

import io.trane.ndbc.postgres.proto.Message.Describe;
import io.trane.ndbc.proto.BufferWriter;

public class DescribeEncoder {

  public final void encode(Describe msg, BufferWriter b) {
    b.writeChar('D');

    if (msg instanceof Describe.DescribePreparedStatement)
      b.writeChar('S');
    else if (msg instanceof Describe.DescribePortal)
      b.writeChar('P');
    else
      throw new IllegalStateException("Invalid describe message: " + msg);

    b.writeCString(msg.name);
    b.writeLength(1);
  }
}
