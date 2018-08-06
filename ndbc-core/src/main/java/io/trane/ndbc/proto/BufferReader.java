package io.trane.ndbc.proto;

import java.nio.charset.Charset;

public interface BufferReader {

  int readableBytes();

  int readInt();

  byte readByte();

  short readShort();

  String readCString(Charset charset);

  String readCString(int length, Charset charset);

  String readString(Charset charset);

  String readString(int length, Charset charset);

  byte[] readBytes();

  byte[] readBytes(int length);

  int[] readInts();

  int[] readInts(int length);

  short[] readShorts();

  short[] readShorts(int length);

  BufferReader readSlice(int length);

  void markReaderIndex();

  void resetReaderIndex();

  void retain();

  void release();

  Long readLong();

  Float readFloat();

  Double readDouble();

}
