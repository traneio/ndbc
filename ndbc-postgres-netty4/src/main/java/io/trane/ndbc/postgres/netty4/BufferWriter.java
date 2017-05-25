package io.trane.ndbc.postgres.netty4;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

public class BufferWriter implements io.trane.ndbc.proto.BufferWriter {

  private final ByteBuf b;
  private final Charset charset;

  public BufferWriter(Charset charset, ByteBuf b) {
    super();
    this.charset = charset;
    this.b = b;
  }

  @Override
  public void writeInt(int i) {
    b.writeInt(i);
  }

  @Override
  public void writeByte(byte v) {
    b.writeByte(v);
  }

  @Override
  public void writeChar(char c) {
    b.writeByte(c);
  }

  @Override
  public void writeShort(short s) {
    b.writeShort(s);
  }

  @Override
  public void writeCString(String s) {
    writeString(s);
    b.writeByte(0);
  }
  
  @Override
  public void writeString(String s) {
    b.writeBytes(s.getBytes(charset));
  }

  @Override
  public void writeBytes(byte[] a) {
    b.writeBytes(a);
  }

  @Override
  public void writeInts(int[] a) {
    for (int i : a)
      b.writeInt(i);
  }

  @Override
  public void writeShorts(short[] a) {
    for (short i : a)
      b.writeShort(i);
  }
  
  @Override
  public int writerIndex() {
    return b.writerIndex();
  }

  @Override
  public void writeLength(int position) {
    int length = b.writerIndex() - position;
    b.markWriterIndex();
    b.writerIndex(position);
    b.writeInt(length);
    b.resetWriterIndex();
  }

  @Override
  public void writeLengthNoSelf(int position) {
    int length = b.writerIndex() - position - 4;
    b.markWriterIndex();
    b.writerIndex(position);
    b.writeInt(length);
    b.resetWriterIndex();
  }
}
