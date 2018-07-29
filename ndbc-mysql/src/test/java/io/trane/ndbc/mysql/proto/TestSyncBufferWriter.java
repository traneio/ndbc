package io.trane.ndbc.mysql.proto;

import io.trane.ndbc.proto.BufferWriter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class TestSyncBufferWriter implements BufferWriter {

	final private DataOutputStream dos;
	final private Charset charset;

	public TestSyncBufferWriter(OutputStream os, Charset charset) {
		this.dos = new DataOutputStream(os);
		this.charset = charset;
	}

	@Override
	public void writeInt(int i) {
		try {
			dos.writeInt(i);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeByte(byte b) {
		try {
			dos.writeByte(b);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void writeChar(char b) {
		try {
			dos.writeChar(b);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeShort(short s) {

		try {
			dos.writeShort(s);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeCString(String s) {
		try {
			dos.write(s.getBytes(charset));
			dos.writeByte(0 & 0xFF);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeString(String s) {
		try {
			dos.write(s.getBytes(charset));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeBytes(byte[] b) {
		try {
			dos.write(b);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void writeInts(int[] is) {
		try {
			for (int i : is) {
				dos.writeInt(i);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeShorts(short[] ss) {
		try {
			for (short s : ss) {
				dos.writeShort(s);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeLength(int position) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeLengthNoSelf(int position) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int writerIndex() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void writeLong(Long value) {
		try {
			dos.writeLong(value);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeFloat(Float value) {
		try {
			dos.writeFloat(value);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void writeDouble(Double value) {

		try {
			dos.writeDouble(value);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
