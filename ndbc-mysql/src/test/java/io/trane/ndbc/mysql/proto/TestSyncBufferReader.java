package io.trane.ndbc.mysql.proto;

import io.trane.ndbc.proto.BufferReader;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TestSyncBufferReader implements BufferReader {

	private DataInputStream dis;
	private Charset charset;

	public TestSyncBufferReader(InputStream is, Charset charset) {
		this.dis = new DataInputStream(is);
		this.charset = charset;
	}

	@Override
	public int readableBytes() {
		try {
			return dis.available();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int readInt() {
		try {
			return dis.readInt();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte readByte() {
		try {
			return dis.readByte();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short readShort() {
		try {
			return dis.readShort();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String readCString() {
		try {
			ArrayList<Byte> byteList = new ArrayList<>();
			byte b = dis.readByte();
			while (b != 0) {
				byteList.add(b);
				b = dis.readByte();
			}
			byte[] arr = new byte[byteList.size()];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = byteList.get(i);
			}
			return new String(arr, this.charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String readCString(int length) {
		try {
			byte[] buffer = new byte[length];
			dis.readFully(buffer);
			dis.readByte();
			return new String(buffer, this.charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String readString() {
		try {
			return readString(dis.available());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String readString(int length) {
		try {
			byte[] buffer = new byte[length];
			dis.readFully(buffer);
			return new String(buffer, this.charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] readBytes() {
		try {
			return readBytes(dis.available());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] readBytes(int length) {
		byte[] buf = new byte[length];
		try {
			dis.available();
			dis.readFully(buf);
			return buf;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int[] readInts() {
		try {
			return readInts(dis.available());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int[] readInts(int length) {
		try {
			int[] buf = new int[length];
			for (int i = 0; i < length; i++) {
				buf[i] = dis.readInt();
			}
			return buf;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short[] readShorts() {
		try {
			return readShorts(dis.available());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short[] readShorts(int length) {
		try {
			short[] buf = new short[length];
			for (int i = 0; i < length; i++) {
				buf[i] = dis.readShort();
			}
			return buf;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override

	public BufferReader readSlice(int length) {
		try {
			byte[] buf = new byte[length];
			dis.readFully(buf);
			return new TestSyncBufferReader(new ByteArrayInputStream(buf), this.charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void markReaderIndex() {
		dis.mark(10);
	}

	@Override
	public void resetReaderIndex() {
		try {
			dis.reset();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void retain() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void release() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Long readLong() {
		try {
			return dis.readLong();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Float readFloat() {
		try {
			return dis.readFloat();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Double readDouble() {
		try {
			return dis.readDouble();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
