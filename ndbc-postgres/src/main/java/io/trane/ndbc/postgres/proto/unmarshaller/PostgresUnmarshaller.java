package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.trane.ndbc.NdbcException;
import io.trane.ndbc.postgres.proto.Message.InfoResponse;
import io.trane.ndbc.postgres.proto.Message.NotificationResponse;
import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.proto.Unmarshaller;

public abstract class PostgresUnmarshaller<T extends ServerMessage> implements Unmarshaller<T> {

  private static final Logger log = LoggerFactory.getLogger(PostgresUnmarshaller.class);

  protected final Charset                      charset;
  private final InfoResponseFieldsUnmarshaller infoResponseFieldsUnmarshaller;

  public PostgresUnmarshaller(final Charset charset) {
    this.charset = charset;
    this.infoResponseFieldsUnmarshaller = new InfoResponseFieldsUnmarshaller(charset);
  }

  @Override
  public Optional<T> apply(final BufferReader b) {
    final byte tpe = b.readByte();
    final int length = b.readInt() - 4;
    return read(tpe, b.readSlice(length));
  }

  private Optional<T> read(final byte tpe, final BufferReader b) {
    switch (tpe) {
      case 'E':
        final InfoResponse.ErrorResponse error = new InfoResponse.ErrorResponse(
            infoResponseFieldsUnmarshaller.apply(b));
        throw new NdbcException(error.toString()); // TODO custom exception
      case 'N':
        final InfoResponse.NoticeResponse notice = new InfoResponse.NoticeResponse(
            infoResponseFieldsUnmarshaller.apply(b));
        log.debug(notice.toString());
        return Optional.empty();
      case 'A':
        final NotificationResponse notification = new NotificationResponse(b.readInt(), b.readCString(charset),
            b.readCString(charset));
        log.info(notification.toString());
        return Optional.empty();
      default:
        if (!acceptsType(tpe))
          throw new IllegalStateException(
              "Unmarshaller " + getClass() + " doesn't accept message type " + (char) tpe);
        return Optional.of(decode(tpe, b));
    }
  }

  public <U extends ServerMessage> PostgresUnmarshaller<ServerMessage> orElse(final PostgresUnmarshaller<U> other) {
    return new PostgresUnmarshaller<ServerMessage>(null) {

      @Override
      protected boolean acceptsType(final byte tpe) {
        return PostgresUnmarshaller.this.acceptsType(tpe) || other.acceptsType(tpe);
      }

      @Override
      protected ServerMessage decode(final byte tpe, final BufferReader readSlice) {
        if (PostgresUnmarshaller.this.acceptsType(tpe))
          return PostgresUnmarshaller.this.decode(tpe, readSlice);
        else
          return other.decode(tpe, readSlice);
      }

      @Override
      public String toString() {
        return PostgresUnmarshaller.this.toString() + ".orElse(" + other.toString() + ")";
      }
    };
  }

  protected abstract boolean acceptsType(byte tpe);

  protected abstract T decode(byte tpe, BufferReader readSlice);

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
