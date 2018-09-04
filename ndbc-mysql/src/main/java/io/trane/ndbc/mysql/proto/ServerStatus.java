package io.trane.ndbc.mysql.proto;

/**
 * Represents the ServerStatus as represented in EOF packets
 *
 * @param mask
 *          the raw bit mask in the EOF packet
 */
public class ServerStatus {

  public enum Flag {
    InTrans(0x0001), Autocommit(0x0002), MoreResultsExists(0x0008), NoGoodIndexUsed(0x0010), NoIndexUsed(
        0x0020), CursorExists(0x0040), LastRowSent(0x0080), DbDropped(0x0100), NoBackslashEscapes(
            0x0200), MetadataChanged(0x0400), QueryWasSlow(
                0x0800), PsOutParams(0x1000), InTransReadonly(0x2000), SessionStateChanged(0x4000);

    private final int value;

    Flag(int value) {
      this.value = value;
    }

    public boolean isSet(int mask) {
      return (value & mask) > 0;
    }
  }

  private final int mask;

  public ServerStatus(int mask) {
    this.mask = mask;
  }

  public boolean isSet(Flag v) {
    return v.isSet(mask);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Flag v : Flag.values())
      if (isSet(v))
        sb.append(v.toString() + " ");
    return "ServerStatus[flags = " + sb + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + mask;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ServerStatus other = (ServerStatus) obj;
    if (mask != other.mask)
      return false;
    return true;
  }
}
