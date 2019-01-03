package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.postgres.proto.Message.PortalSuspended;
import io.trane.ndbc.proto.BufferReader;

final class PortalSuspendedUnmarshaller extends PostgresUnmarshaller<PortalSuspended> {

  private final PortalSuspended portalSuspended = new PortalSuspended();

  public PortalSuspendedUnmarshaller(final Charset charset) {
    super(charset);
  }

  @Override
  protected boolean acceptsType(final byte tpe) {
    return tpe == 's';
  }

  @Override
  public final PortalSuspended decode(final byte tpe, final BufferReader b) {
    return portalSuspended;
  }
}
