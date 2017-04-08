package io.trane.ndbc.postgres.proto.serializer;

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

public class Serializer {

  private static final Logger log = Logger.getLogger(Serializer.class.getName());

  private final BindSerializer bindEncoder = new BindSerializer();
  private final CancelRequestSerializer cancelRequestEncoder = new CancelRequestSerializer();
  private final CloseSerializer closeEncoder = new CloseSerializer();
  private final DescribeSerializer describeEncoder = new DescribeSerializer();
  private final ExecuteSerializer executeEncoder = new ExecuteSerializer();
  private final FlushSerializer flushEncoder = new FlushSerializer();
  private final ParseSerializer parseEncoder = new ParseSerializer();
  private final QuerySerializer queryEncoder = new QuerySerializer();
  private final PasswordMessageSerializer passwordMessageEncoder = new PasswordMessageSerializer();
  private final StartupMessageSerializer startupMessageEncoder = new StartupMessageSerializer();
  private final SyncSerializer syncEncoder = new SyncSerializer();
  private final TerminateSerializer terminateEncoder = new TerminateSerializer();

  public final void encode(ClientMessage msg, BufferWriter b) {
    if (msg instanceof Bind)
      bindEncoder.encode((Bind) msg, b);
    else if (msg instanceof CancelRequest)
      cancelRequestEncoder.encode((CancelRequest) msg, b);
    else if (msg instanceof Close)
      closeEncoder.encode((Close) msg, b);
    else if (msg instanceof CopyData)
      notImplemented(CopyData.class);
    else if (msg instanceof CopyDone)
      notImplemented(CopyDone.class);
    else if (msg instanceof CopyFail)
      notImplemented(CopyFail.class);
    else if (msg instanceof Describe)
      describeEncoder.encode((Describe) msg, b);
    else if (msg instanceof Execute)
      executeEncoder.encode((Execute) msg, b);
    else if (msg instanceof FlushSerializer)
      flushEncoder.encode((Flush) msg, b);
    else if (msg instanceof FunctionCall)
      notImplemented(FunctionCall.class);
    else if (msg instanceof Parse)
      parseEncoder.encode((Parse) msg, b);
    else if (msg instanceof Query)
      queryEncoder.encode((Query) msg, b);
    else if (msg instanceof PasswordMessage)
      passwordMessageEncoder.encode((PasswordMessage) msg, b);
    else if (msg instanceof SSLRequest)
      notImplemented(SSLRequest.class);
    else if (msg instanceof StartupMessage)
      startupMessageEncoder.encode((StartupMessage) msg, b);
    else if (msg instanceof Sync)
      syncEncoder.encode((Flush) msg, b);
    else if (msg instanceof Terminate)
      terminateEncoder.encode((Flush) msg, b);
    else
      log.severe("Invalid client message: " + msg);
  }

  private void notImplemented(Class<? extends ClientMessage> cls) {
    throw new UnsupportedOperationException("Decoder not implemented for class: " + cls);
  }
}
