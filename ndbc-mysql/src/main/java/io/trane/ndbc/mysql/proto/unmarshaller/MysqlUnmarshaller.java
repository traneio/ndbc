package io.trane.ndbc.mysql.proto.unmarshaller;

import java.util.Optional;

import io.trane.ndbc.mysql.proto.Message.ServerMessage;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.Unmarshaller;

public abstract class MysqlUnmarshaller<T extends ServerMessage> implements Unmarshaller<T> {

  @Override
  public Optional<T> apply(final BufferReader bufferReader) {
    final PacketBufferReader p = new PacketBufferReader(bufferReader);
    p.markReaderIndex();
    final int header = p.readByte() & 0xFF;
    if (!acceptsHeader(header, p.readableBytes()))
      throw new IllegalStateException(
          "Invalid packet for " + getClass() + ". Remaining bytes: " + bufferReader.readableBytes());
    p.resetReaderIndex();
    return Optional.of(decode(p));
  }

  public <U extends ServerMessage> MysqlUnmarshaller<ServerMessage> orElse(final MysqlUnmarshaller<U> other) {

    return new MysqlUnmarshaller<ServerMessage>() {

      @Override
      protected boolean acceptsHeader(final int header, final int readableBytes) {
        return MysqlUnmarshaller.this.acceptsHeader(header, readableBytes)
            || other.acceptsHeader(header, readableBytes);
      }

      @Override
      protected ServerMessage decode(final PacketBufferReader p) {
        p.markReaderIndex();
        final int header = p.readByte() & 0xFF;
        final int readableBytes = p.readableBytes();
        p.resetReaderIndex();
        if (MysqlUnmarshaller.this.acceptsHeader(header, readableBytes))
          return MysqlUnmarshaller.this.decode(p);
        else
          return other.decode(p);
      }

      @Override
      public String toString() {
        return MysqlUnmarshaller.this.toString() + ".orElse(" + other.toString() + ")";
      }
    };
  }

  protected boolean acceptsHeader(final int header, final int readableBytes) {
    return true;
  }

  protected abstract T decode(PacketBufferReader packet);

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
