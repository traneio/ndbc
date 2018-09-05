package io.trane.ndbc.postgres.proto.unmarshaller;

import java.util.Optional;
import java.util.function.Function;

import io.trane.ndbc.proto.BufferReader;

public class TransformBufferReader implements Function<BufferReader, Optional<BufferReader>> {

  @Override
  public Optional<BufferReader> apply(final BufferReader b) {
    if (b.readableBytes() >= 5) {
      b.markReaderIndex();
      b.readByte(); // tpe
      final int length = b.readInt() - 4;
      if (b.readableBytes() >= length) {
        b.resetReaderIndex();
        final BufferReader slice = b.readSlice(length + 5);
        slice.retain();
        return Optional.of(slice);
      } else {
        b.resetReaderIndex();
        return Optional.empty();
      }
    } else
      return Optional.empty();
  }
}
