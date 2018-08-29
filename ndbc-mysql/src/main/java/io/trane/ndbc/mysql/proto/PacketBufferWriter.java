package io.trane.ndbc.mysql.proto;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import io.trane.ndbc.proto.BufferWriter;

public class PacketBufferWriter implements BufferWriter {

  final private BufferWriter          bw;
  final private ByteArrayOutputStream baos;
  final private DataOutputStream      dos;
  final private int                   sequence;

  public PacketBufferWriter(final BufferWriter bw, final int sequence) {
    this.baos = new ByteArrayOutputStream();
    this.dos = new DataOutputStream(baos);
    this.bw = bw;
    this.sequence = sequence;
  }

  @Override
  public void writeInt(final int i) {
    try {
      dos.writeInt(i);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeUnsignedInt(final long ui) {
    final byte[] bytes = { ((byte) (ui)), ((byte) (ui >> 8)), ((byte) (ui >> 16)), ((byte) (ui >> 24)) };
    writeBytes(bytes);
  }

  public void writeUnsignedShort(final int s) {
    final byte[] bytes = { (byte) (s & 0xff), (byte) ((s >> 8) & 0xff) };
    writeBytes(bytes);
  }

  @Override
  public void writeByte(final byte b) {
    try {
      dos.writeByte(b);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeChar(final char b) {
    try {
      dos.writeChar(b);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeShort(final short s) {

    try {
      dos.writeShort(s);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeCString(final String s) {
    try {
      dos.write(s.getBytes(bw.getCharset()));
      dos.writeByte(0);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeString(final String s) {
    try {
      dos.write(s.getBytes(bw.getCharset()));
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  private final void writeLength(long length) {
    if (length < 251) {
      writeByte((byte) length);
    } else if (length < 65536) {
      writeByte((byte) 252);
      writeUnsignedShort((int) length);
    } else if (length < 16777216) {
      writeByte((byte) 253);
      writeMediumLE(length);
    } else {
      writeByte((byte) 254);
      writeLongLE(length)
    }
  }

  public void writeLengthCodedString(Charset charset, String value) {
    byte[] bytes = value.getBytes(bw.getCharset());
    bw.writeByte((byte) (bytes.length | 0xFF));
    bw.writeBytes(bytes);
  }

  @Override
  public void writeBytes(final byte[] b) {
    try {
      dos.write(b);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void writeInts(final int[] is) {
    try {
      for (final int i : is) {
        dos.writeInt(i);
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeShorts(final short[] ss) {
    try {
      for (final short s : ss) {
        dos.writeShort(s);
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeLength(final int position) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeLengthNoSelf(final int position) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int writerIndex() {
    throw new UnsupportedOperationException();

  }

  @Override
  public void writeLong(final Long value) {
    try {
      dos.writeLong(value);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeFloat(final Float value) {
    try {
      dos.writeFloat(value);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void writeDouble(final Double value) {
    try {
      dos.writeDouble(value);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void flush() {

    final int packetLength = baos.size();

    final byte[] header = { (byte) (packetLength & 0xff), (byte) ((packetLength >> 8) & 0xff),
        (byte) ((packetLength >> 16) & 0xff), (byte) (this.sequence) };
    bw.writeBytes(concat(header, baos.toByteArray()));

  }

  private byte[] concat(final byte[] a, final byte[] b) {
    final byte[] c = new byte[a.length + b.length];
    System.arraycopy(a, 0, c, 0, a.length);
    System.arraycopy(b, 0, c, a.length, b.length);
    return c;
  }

  @Override
  public Charset getCharset() {
    return bw.getCharset();
  }
}
