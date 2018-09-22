package io.trane.ndbc.sqlserver.proto.unmarshaller;

import java.util.Optional;
import java.util.function.Function;

import io.trane.ndbc.proto.BufferReader;

public class TransformBufferReader implements Function<BufferReader, Optional<BufferReader>> {

  @Override
  public Optional<BufferReader> apply(final BufferReader b) {
    return Optional.empty();
  }
}
