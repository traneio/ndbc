package io.trane.ndbc.postgres.proto.marshaller;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.trane.ndbc.postgres.proto.Message.Bind;
import io.trane.ndbc.postgres.proto.Message.CancelRequest;
import io.trane.ndbc.postgres.proto.Message.Close;
import io.trane.ndbc.postgres.proto.Message.CopyData;
import io.trane.ndbc.postgres.proto.Message.CopyDone;
import io.trane.ndbc.postgres.proto.Message.CopyFail;
import io.trane.ndbc.postgres.proto.Message.Describe;
import io.trane.ndbc.postgres.proto.Message.Execute;
import io.trane.ndbc.postgres.proto.Message.Flush;
import io.trane.ndbc.postgres.proto.Message.FunctionCall;
import io.trane.ndbc.postgres.proto.Message.Parse;
import io.trane.ndbc.postgres.proto.Message.PasswordMessage;
import io.trane.ndbc.postgres.proto.Message.Query;
import io.trane.ndbc.postgres.proto.Message.SSLRequest;
import io.trane.ndbc.postgres.proto.Message.StartupMessage;
import io.trane.ndbc.postgres.proto.Message.Sync;
import io.trane.ndbc.postgres.proto.Message.Terminate;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.ClientMessage;
import io.trane.ndbc.proto.Marshaller;

public class PostgresMarshaller implements Marshaller {

	private static final Logger log = Logger.getLogger(PostgresMarshaller.class.getName());

	private final BindMarshaller bindMarshaller;
	private final CancelRequestMarshaller cancelRequestMarshaller;
	private final CloseMarshaller closeMarshaller;
	private final DescribeMarshaller describeMarshaller;
	private final ExecuteMarshaller executeMarshaller;
	private final FlushMarshaller flushMarshaller;
	private final ParseMarshaller parseMarshaller;
	private final QueryMarshaller queryMarshaller;
	private final PasswordMessageMarshaller passwordMessageMarshaller;
	private final StartupMessageMarshaller startupMessageMarshaller;
	private final SyncMarshaller syncMarshaller;
	private final TerminateMarshaller terminateMarshaller;
	private final SSLRequestMarshaller sslRequestMarshaller;

	public PostgresMarshaller(final BindMarshaller bindMarshaller,
			final CancelRequestMarshaller cancelRequestMarshaller, final CloseMarshaller closeMarshaller,
			final DescribeMarshaller describeMarshaller, final ExecuteMarshaller executeMarshaller,
			final FlushMarshaller flushMarshaller, final ParseMarshaller parseMarshaller,
			final QueryMarshaller queryMarshaller, final PasswordMessageMarshaller passwordMessageMarshaller,
			final StartupMessageMarshaller startupMessageMarshaller, final SyncMarshaller syncMarshaller,
			final TerminateMarshaller terminateMarshaller, final SSLRequestMarshaller sslRequestMarshaller) {
		super();
		this.bindMarshaller = bindMarshaller;
		this.cancelRequestMarshaller = cancelRequestMarshaller;
		this.closeMarshaller = closeMarshaller;
		this.describeMarshaller = describeMarshaller;
		this.executeMarshaller = executeMarshaller;
		this.flushMarshaller = flushMarshaller;
		this.parseMarshaller = parseMarshaller;
		this.queryMarshaller = queryMarshaller;
		this.passwordMessageMarshaller = passwordMessageMarshaller;
		this.startupMessageMarshaller = startupMessageMarshaller;
		this.syncMarshaller = syncMarshaller;
		this.terminateMarshaller = terminateMarshaller;
		this.sslRequestMarshaller = sslRequestMarshaller;
	}

	@Override
  public final void encode(final ClientMessage msg, final BufferWriter b) {
		try {
			if (msg instanceof Bind)
				bindMarshaller.encode((Bind) msg, b);
			else if (msg instanceof CancelRequest)
				cancelRequestMarshaller.encode((CancelRequest) msg, b);
			else if (msg instanceof Close)
				closeMarshaller.encode((Close) msg, b);
			else if (msg instanceof CopyData)
				notImplemented(CopyData.class);
			else if (msg instanceof CopyDone)
				notImplemented(CopyDone.class);
			else if (msg instanceof CopyFail)
				notImplemented(CopyFail.class);
			else if (msg instanceof Describe)
				describeMarshaller.encode((Describe) msg, b);
			else if (msg instanceof Execute)
				executeMarshaller.encode((Execute) msg, b);
			else if (msg instanceof Flush)
				flushMarshaller.encode((Flush) msg, b);
			else if (msg instanceof FunctionCall)
				notImplemented(FunctionCall.class);
			else if (msg instanceof Parse)
				parseMarshaller.encode((Parse) msg, b);
			else if (msg instanceof Query)
				queryMarshaller.encode((Query) msg, b);
			else if (msg instanceof PasswordMessage)
				passwordMessageMarshaller.encode((PasswordMessage) msg, b);
			else if (msg instanceof SSLRequest)
				sslRequestMarshaller.encode((SSLRequest) msg, b);
			else if (msg instanceof StartupMessage)
				startupMessageMarshaller.encode((StartupMessage) msg, b);
			else if (msg instanceof Sync)
				syncMarshaller.encode((Sync) msg, b);
			else if (msg instanceof Terminate)
				terminateMarshaller.encode((Terminate) msg, b);
			else
				log.severe("Invalid client message: " + msg);
		} catch (final Exception e) {
			log.log(Level.SEVERE, "Can't serialize client message " + msg, e);
			throw e;
		}
	}

	private void notImplemented(final Class<? extends ClientMessage> cls) {
		throw new UnsupportedOperationException("Decoder not implemented for class: " + cls);
	}
}
