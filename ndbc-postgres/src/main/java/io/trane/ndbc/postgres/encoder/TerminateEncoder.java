package io.trane.ndbc.postgres.encoder;

import io.trane.ndbc.postgres.proto.Message.Flush;
import io.trane.ndbc.proto.BufferWriter;

public class TerminateEncoder {

  public final void encode(Flush msg, BufferWriter b) {
    b.writeChar('X');
    b.writeLength(1);
  }
}
