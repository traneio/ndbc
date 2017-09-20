package io.trane.ndbc.mysql.proto;


import io.trane.ndbc.proto.BufferWriter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class PacketBufferWriter implements BufferWriter {


  final private BufferWriter bw;
  final private ByteArrayOutputStream baos;
  final private DataOutputStream dos;
  final private int sequence;
  final private Charset charset;

  public PacketBufferWriter( BufferWriter bw,  int sequence, Charset charset) {
    this.baos = new ByteArrayOutputStream();
    this.dos = new DataOutputStream(baos);
    this.bw = bw;
    this.sequence = sequence;
    this.charset = charset;
  }

  public void writeInt(int i) {
    try {
      dos.writeInt(i);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeUnsignedInt(long ui) {
    byte[] bytes =  {
            ((byte) (ui)),
            ((byte) (ui >> 8)),
            ((byte) (ui >> 16)),
            ((byte) (ui >> 24))};
    writeBytes(bytes);
  }

  public void writeByte(byte b) {
    try {
      dos.writeByte(b);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeChar(char b) {
    try {
      dos.writeChar(b);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeShort(short s) {

    try {
      dos.writeShort(s);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeCString(String s) {
    try {
      dos.write(s.getBytes(charset));
      dos.writeByte(0);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeString(String s) {
    try {
      dos.write(s.getBytes(charset));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeBytes(byte[] b) {
    try {
      dos.write(b);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public void writeInts(int[] is) {
    try {
      for (int i:is) {
        dos.writeInt(i);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeShorts(short[] ss) {
    try {
      for (short s:ss) {
        dos.writeShort(s);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeLength(int position) {
    throw new UnsupportedOperationException();
  }

  public void writeLengthNoSelf(int position) {
    throw new UnsupportedOperationException();
  }

  public int writerIndex() {
    throw new UnsupportedOperationException();

  }

  public void writeLong(Long value) {
    try {
      dos.writeLong(value);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeFloat(Float value) {
    try {
      dos.writeFloat(value);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public void writeDouble(Double value) {

    try {
      dos.writeDouble(value);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void flush() {

    int packetLength = baos.size();

    byte[] header = {
            (byte)(packetLength & 0xff),
            (byte)((packetLength >> 8) & 0xff),
            (byte)((packetLength >> 16) & 0xff),
            (byte)(this.sequence)
    };
    bw.writeBytes(concat(header, baos.toByteArray()));

  }

  private byte[] concat(byte[] a, byte[] b) {
    byte[] c = new byte[a.length + b.length];
    System.arraycopy(a, 0, c, 0, a.length);
    System.arraycopy(b, 0, c, a.length, b.length);
    return c;
  }
}
