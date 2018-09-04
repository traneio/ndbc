package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.postgres.proto.Message.CopyDone;
import io.trane.ndbc.proto.BufferReader;;

public final class CopyDoneUnmarshaller extends PostgresUnmarshaller<CopyDone> {

	private static final CopyDone copyDone = new CopyDone();

	public CopyDoneUnmarshaller(Charset charset) {
		super(charset);
	}

	@Override
	protected boolean acceptsType(byte tpe) {
		return tpe == 'c';
	}

	@Override
	public final CopyDone decode(final byte tpe, final BufferReader b) {
		return copyDone;
	}
}
