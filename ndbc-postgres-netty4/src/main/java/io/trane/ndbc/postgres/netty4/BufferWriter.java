package io.trane.ndbc.postgres.netty4;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

final class BufferWriter implements io.trane.ndbc.proto.BufferWriter {

  private final ByteBuf b;
  private final Charset charset;

  public BufferWriter(final Charset charset, final ByteBuf b) {
    super();
    this.charset = charset;
    this.b = b;
  }

  @Override
  public final void writeInt(final int i) {
    b.writeInt(i);
  }

  @Override
  public final void writeByte(final byte v) {
    b.writeByte(v);
  }

  @Override
  public final void writeChar(final char c) {
    b.writeByte(c);
  }

  @Override
  public final void writeShort(final short s) {
    b.writeShort(s);
  }

  @Override
  public final void writeCString(final String s) {
    writeString(s);
    b.writeByte(0);
  }

  @Override
  public final void writeString(final String s) {
    b.writeBytes(s.getBytes(charset));
  }

  @Override
  public final void writeBytes(final byte[] a) {
    b.writeBytes(a);
  }

  @Override
  public final void writeInts(final int[] a) {
    for (final int i : a)
      b.writeInt(i);
  }

  @Override
  public final void writeShorts(final short[] a) {
    for (final short i : a)
      b.writeShort(i);
  }

  @Override
  public final void writeLong(final Long value) {
    b.writeLong(value);
  }

  @Override
  public final void writeFloat(final Float value) {
    b.writeFloat(value);
  }

  @Override
  public final void writeDouble(final Double value) {
    b.writeDouble(value);
  }

  @Override
  public final int writerIndex() {
    return b.writerIndex();
  }

  @Override
  public final void writeLength(final int position) {
    final int length = b.writerIndex() - position;
    b.markWriterIndex();
    b.writerIndex(position);
    b.writeInt(length);
    b.resetWriterIndex();
  }

  @Override
  public final void writeLengthNoSelf(final int position) {
    final int length = b.writerIndex() - position - 4;
    b.markWriterIndex();
    b.writerIndex(position);
    b.writeInt(length);
    b.resetWriterIndex();
  }
}
