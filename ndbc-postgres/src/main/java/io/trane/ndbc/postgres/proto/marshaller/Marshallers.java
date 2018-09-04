package io.trane.ndbc.postgres.proto.marshaller;

import io.trane.ndbc.postgres.encoding.EncodingRegistry;

public final class Marshallers {

	public final BindMarshaller bind;
	public final CancelRequestMarshaller cancelRequest;
	public final CloseMarshaller close;
	public final DescribeMarshaller describe;
	public final ExecuteMarshaller execute;
	public final FlushMarshaller flush;
	public final ParseMarshaller parse;
	public final PasswordMessageMarshaller passwordMessage;
	public final QueryMarshaller query;
	public final SSLRequestMarshaller sslRequest;
	public final StartupMessageMarshaller startupMessage;
	public final SyncMarshaller sync;
	public final TerminateMarshaller terminate;

	public Marshallers(EncodingRegistry encoding) {
		bind = new BindMarshaller(encoding);
		cancelRequest = new CancelRequestMarshaller();
		close = new CloseMarshaller();
		describe = new DescribeMarshaller();
		execute = new ExecuteMarshaller();
		flush = new FlushMarshaller();
		parse = new ParseMarshaller(encoding);
		passwordMessage = new PasswordMessageMarshaller();
		query = new QueryMarshaller();
		sslRequest = new SSLRequestMarshaller();
		startupMessage = new StartupMessageMarshaller();
		sync = new SyncMarshaller();
		terminate = new TerminateMarshaller();
	}
}
