package io.trane.ndbc.mysql.proto;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import io.trane.ndbc.proto.BufferWriter;

public class TestSyncBufferWriter implements BufferWriter {

	final private DataOutputStream dos;
	final private Charset charset;

	public TestSyncBufferWriter(final OutputStream os, final Charset charset) {
		this.dos = new DataOutputStream(os);
		this.charset = charset;
	}

	@Override
	public void writeInt(final int i) {
		try {
			dos.writeInt(i);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
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
			dos.write(s.getBytes(charset));
			dos.writeByte(0 & 0xFF);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeString(final String s) {
		try {
			dos.write(s.getBytes(charset));
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
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
}
