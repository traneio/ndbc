package io.trane.ndbc.postgres.encoder;

import io.trane.ndbc.postgres.proto.Message;
import io.trane.ndbc.postgres.proto.Message.*;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.ClientMessage;

public class Encoder {

  private final BindEncoder bindEncoder = new BindEncoder();
  private final CancelRequestEncoder cancelRequestEncoder = new CancelRequestEncoder();
  private final CloseEncoder closeEncoder = new CloseEncoder();
  private final DescribeEncoder describeEncoder = new DescribeEncoder();
  private final ExecuteEncoder executeEncoder = new ExecuteEncoder();
  private final FlushEncoder flushEncoder = new FlushEncoder();
  private final ParseEncoder parseEncoder = new ParseEncoder();
  private final PasswordMessageEncoder passwordMessageEncoder = new PasswordMessageEncoder();
  private final StartupMessageEncoder startupMessageEncoder = new StartupMessageEncoder();
  private final SyncEncoder syncEncoder = new SyncEncoder();
  private final TerminateEncoder terminateEncoder = new TerminateEncoder();

  public final void encode(Message msg, BufferWriter b) {
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
    else if (msg instanceof FlushEncoder)
      flushEncoder.encode((Flush) msg, b);
    else if (msg instanceof FunctionCall)
      notImplemented(FunctionCall.class);
    else if (msg instanceof Parse)
      parseEncoder.encode((Parse) msg, b);
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
      throw new IllegalStateException("Invalid client message: " + msg);
  }

  private void notImplemented(Class<? extends ClientMessage> cls) {
    throw new UnsupportedOperationException("Decoder not implemented for class: " + cls);
  }
}
