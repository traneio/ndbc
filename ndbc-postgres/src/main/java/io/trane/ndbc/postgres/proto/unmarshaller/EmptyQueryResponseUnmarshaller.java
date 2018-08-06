package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.postgres.proto.Message.EmptyQueryResponse;
import io.trane.ndbc.proto.BufferReader;;

public final class EmptyQueryResponseUnmarshaller extends PostgresUnmarshaller<EmptyQueryResponse> {

  private static final EmptyQueryResponse emptyQueryResponse = new EmptyQueryResponse();

  public EmptyQueryResponseUnmarshaller(Charset charset) {
    super(charset);
    // TODO Auto-generated constructor stub
  }

  @Override
  protected boolean acceptsType(byte tpe) {
    return tpe == 'I';
  }

  @Override
  public final EmptyQueryResponse decode(final byte tpe, final BufferReader b) {
    return emptyQueryResponse;
  }
}
