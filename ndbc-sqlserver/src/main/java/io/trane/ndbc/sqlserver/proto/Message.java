package io.trane.ndbc.sqlserver.proto;

public interface Message {

  interface ServerMessage extends io.trane.ndbc.proto.ServerMessage, Message {
  }

  interface ClientMessage extends io.trane.ndbc.proto.ClientMessage, Message {
  }

  public static final class PreLogin implements ClientMessage {
    public final byte header = (byte) 0x12;

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (header ^ header >>> 32);
      return result;
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final PreLogin other = (PreLogin) obj;
      if (header != other.header)
        return false;
      return true;
    }

    @Override
    public String toString() {
      return "PreLogin[header=" + header + "]";
    }
  }

  public static final class Login implements ClientMessage {
  }

  public static final class SqlBatch implements ClientMessage {
  }

  public static final class BulkLoad implements ClientMessage {
  }

  public static final class RemoteProcedureCall implements ClientMessage {
  }

  public static final class Attention implements ClientMessage {
  }

  public static final class TransactionManagerRequest implements ClientMessage {
  }

  public static final class PreLoginResponse implements ServerMessage {
    public Long id = 1234567890L;

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (int) (prime * result + (id ^ id >>> 32));
      return result;
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final PreLoginResponse other = (PreLoginResponse) obj;
      if (id != other.id)
        return false;
      return true;
    }

    @Override
    public String toString() {
      return "PreLoginResponse[id=" + id + "]";
    }
  }

  public static final class LoginResponse implements ServerMessage {
  }

  public static final class RowData implements ServerMessage {
  }

  public static final class Status implements ServerMessage {
  }

  public static final class Parameters implements ServerMessage {
  }

  public static final class Done implements ServerMessage {
  }
}
