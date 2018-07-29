package io.trane.ndbc.mysql.proto;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import io.trane.ndbc.proto.BufferReader;

public class TestSyncBufferReader implements BufferReader {

	private final DataInputStream dis;
	private final Charset charset;

	public TestSyncBufferReader(final InputStream is, final Charset charset) {
		this.dis = new DataInputStream(is);
		this.charset = charset;
	}

	@Override
	public int readableBytes() {
		try {
			return dis.available();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int readInt() {
		try {
			return dis.readInt();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte readByte() {
		try {
			return dis.readByte();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short readShort() {
		try {
			return dis.readShort();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String readCString() {
		try {
			final ArrayList<Byte> byteList = new ArrayList<>();
			byte b = dis.readByte();
			while (b != 0) {
				byteList.add(b);
				b = dis.readByte();
			}
			final byte[] arr = new byte[byteList.size()];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = byteList.get(i);
			}
			return new String(arr, this.charset);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String readCString(final int length) {
		try {
			final byte[] buffer = new byte[length];
			dis.readFully(buffer);
			dis.readByte();
			return new String(buffer, this.charset);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String readString() {
		try {
			return readString(dis.available());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String readString(final int length) {
		try {
			final byte[] buffer = new byte[length];
			dis.readFully(buffer);
			return new String(buffer, this.charset);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] readBytes() {
		try {
			return readBytes(dis.available());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] readBytes(final int length) {
		final byte[] buf = new byte[length];
		try {
			dis.available();
			dis.readFully(buf);
			return buf;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int[] readInts() {
		try {
			return readInts(dis.available());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int[] readInts(final int length) {
		try {
			final int[] buf = new int[length];
			for (int i = 0; i < length; i++) {
				buf[i] = dis.readInt();
			}
			return buf;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short[] readShorts() {
		try {
			return readShorts(dis.available());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short[] readShorts(final int length) {
		try {
			final short[] buf = new short[length];
			for (int i = 0; i < length; i++) {
				buf[i] = dis.readShort();
			}
			return buf;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override

	public BufferReader readSlice(final int length) {
		try {
			final byte[] buf = new byte[length];
			dis.readFully(buf);
			return new TestSyncBufferReader(new ByteArrayInputStream(buf), this.charset);
		} catch (final IOException e) {
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
		} catch (final IOException e) {
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
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Float readFloat() {
		try {
			return dis.readFloat();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Double readDouble() {
		try {
			return dis.readDouble();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
