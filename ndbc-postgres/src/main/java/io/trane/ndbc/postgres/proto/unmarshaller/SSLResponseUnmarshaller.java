package io.trane.ndbc.postgres.proto.unmarshaller;

import java.util.Optional;

import io.trane.ndbc.postgres.proto.Message.SSLResponse;
import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.Unmarshaller;;

public final class SSLResponseUnmarshaller implements Unmarshaller<SSLResponse> {

  private static final Optional<SSLResponse> disabled = Optional.of(new SSLResponse(false));
  private static final Optional<SSLResponse> enabled  = Optional.of(new SSLResponse(true));

  @Override
  public Optional<SSLResponse> apply(BufferReader b) {
    switch (b.readByte()) {
      case 'N':
        return disabled;
      case 'S':
        return enabled;
      default:
        throw new IllegalStateException("Invalid SSL response");
    }
  }
}
