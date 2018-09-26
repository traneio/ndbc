package io.trane.ndbc.sqlserver.proto.marshaller;

import java.util.UUID;

public class ActivityId {
  private final UUID Id;
  private long Sequence;
  private boolean isSentToServer;

  ActivityId() {
    Id = UUID.randomUUID();
    Sequence = 0;
    isSentToServer = false;
  }

  public UUID getId() {
    return Id;
  }

  public long getSequence() {
    return Sequence;
  }

  void Increment() {
    if (Sequence < 0xffffffffl)
      ++Sequence;
    else
      Sequence = 0;

    isSentToServer = false;
  }

  void setSentFlag() {
    isSentToServer = true;
  }

  boolean IsSentToServer() {
    return isSentToServer;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append(Id.toString());
    sb.append("-");
    sb.append(Sequence);
    return sb.toString();
  }
}
