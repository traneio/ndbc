package io.trane.ndbc.postgres.proto.marshaller;

import io.trane.ndbc.postgres.proto.Message.StartupMessage;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;

public final class StartupMessageMarshaller implements Marshaller<StartupMessage> {

  @Override
  public final void apply(final StartupMessage msg, final BufferWriter b) {
    b.writeInt(0);
    b.writeShort((short) 3);
    b.writeShort((short) 0);

    b.writeCString("user");
    b.writeCString(msg.user);

    for (final StartupMessage.Parameter p : msg.parameters) {
      b.writeCString(p.name);
      b.writeCString(p.value);
    }

    b.writeByte((byte) 0);
    b.writeLength(0);
  }
}
