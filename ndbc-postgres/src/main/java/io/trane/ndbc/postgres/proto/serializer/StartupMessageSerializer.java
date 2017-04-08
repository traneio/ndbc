package io.trane.ndbc.postgres.proto.serializer;

import io.trane.ndbc.postgres.proto.Message.StartupMessage;
import io.trane.ndbc.proto.BufferWriter;

public class StartupMessageSerializer {

  public final void encode(StartupMessage msg, BufferWriter b) {
    b.writeInt(0);
    b.writeShort((short) 3);
    b.writeShort((short) 0);

    b.writeCString("user");
    b.writeCString(msg.user);

    for (StartupMessage.Parameter p : msg.parameters) {
      b.writeCString(p.name);
      b.writeCString(p.value);
    }

    b.writeByte((byte) 0);
    b.writeLength(0);
  }
}
