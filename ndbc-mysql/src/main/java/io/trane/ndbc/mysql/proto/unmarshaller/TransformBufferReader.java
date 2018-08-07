package io.trane.ndbc.mysql.proto.unmarshaller;

import java.util.Optional;
import java.util.function.Function;

import io.trane.ndbc.proto.BufferReader;

public class TransformBufferReader implements Function<BufferReader, Optional<BufferReader>> {

  @Override
  public Optional<BufferReader> apply(BufferReader b) {
    b.markReaderIndex();
    final byte[] packetHeader = b.readBytes(4);
    int packetLength = (packetHeader[0] & 0xff) + ((packetHeader[1] & 0xff) << 8)
        + ((packetHeader[2] & 0xff) << 16);
    final int readableBytes = b.readableBytes();
    b.resetReaderIndex();
    if (readableBytes >= packetLength) {
      BufferReader slice = b.readSlice(packetLength + 4);
      slice.retain();
      return Optional.of(slice);
    } else
      return Optional.empty();
  }
}
