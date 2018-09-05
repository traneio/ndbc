package io.trane.ndbc.postgres.proto.marshaller;

import io.trane.ndbc.postgres.proto.Message.SSLRequest;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;

public final class SSLRequestMarshaller implements Marshaller<SSLRequest> {

  @Override
  public final void apply(final SSLRequest msg, final BufferWriter b) {
    b.writeInt(8);
    b.writeInt(80877103);
  }
}
