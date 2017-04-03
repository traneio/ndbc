package io.trane.ndbc.proto;

public interface BufferReader {

  int readableBytes();

  int readInt();

  byte readByte();

  short readShort();

  String readCString();

  String readCString(int length);

  String readString();

  String readString(int length);

  byte[] readBytes();

  byte[] readBytes(int length);

  int[] readInts();

  int[] readInts(int length);

  short[] readShorts();

  short[] readShorts(int length);

  BufferReader readSlice(int length);
}
