package io.trane.ndbc.netty4;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

public final class BufferReader implements io.trane.ndbc.proto.BufferReader {

	private final ByteBuf bb;

	public BufferReader(final ByteBuf bb) {
		this.bb = bb;
	}

	@Override
	public final int readInt() {
		return bb.readInt();
	}

	@Override
	public final short readShort() {
		return bb.readShort();
	}

	@Override
	public final String readCString(Charset charset) {
		final int stringSize = bb.bytesBefore((byte) 0);
		final String string = readString(stringSize, charset);
		bb.skipBytes(1); // skip 0
		return string;
	}

	@Override
	public final String readCString(final int length, Charset charset) {
		final String string = readString(length, charset);
		bb.skipBytes(1); // 0
		return string;
	}

	@Override
	public final String readString(Charset charset) {
		return readString(bb.readableBytes(), charset);
	}

	@Override
	public final String readString(final int length, Charset charset) {
		final byte[] b = new byte[length];
		bb.readBytes(b);
		final String string = new String(b, charset);
		return string;
	}

	@Override
	public final int readableBytes() {
		return bb.readableBytes();
	}

	@Override
	public final byte readByte() {
		return bb.readByte();
	}

	@Override
	public final byte[] readBytes() {
		return readBytes(bb.readableBytes());
	}

	@Override
	public final byte[] readBytes(final int length) {
		final byte[] bytes = new byte[length];
		bb.readBytes(bytes);
		return bytes;
	}

	@Override
	public final BufferReader readSlice(final int length) {
		return new BufferReader(bb.readSlice(length));
	}

	@Override
	public final int[] readInts() {
		return readInts(bb.readableBytes() / 4);
	}

	@Override
	public final int[] readInts(final int length) {
		final int[] ints = new int[length];
		for (int i = 0; i < length; i++)
			ints[i] = bb.readInt();
		return ints;
	}

	@Override
	public final short[] readShorts() {
		return readShorts(bb.readableBytes() / 2);
	}

	@Override
	public final short[] readShorts(final int length) {
		final short[] shorts = new short[length];
		for (int i = 0; i < length; i++)
			shorts[i] = bb.readShort();
		return shorts;
	}

	@Override
	public final Long readLong() {
		return bb.readLong();
	}

	@Override
	public final Float readFloat() {
		return bb.readFloat();
	}

	@Override
	public final Double readDouble() {
		return bb.readDouble();
	}

	@Override
	public final void retain() {
		bb.retain();
	}

	@Override
	public final void release() {
		bb.release();
	}

	@Override
	public final void markReaderIndex() {
		bb.markReaderIndex();
	}

	@Override
	public final void resetReaderIndex() {
		bb.resetReaderIndex();
	}
}