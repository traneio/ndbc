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
  
  private final BindSerializer bindSerializer;
  private final CancelRequestSerializer cancelRequestSerializer;
  private final CloseSerializer closeSerializer;
  private final DescribeSerializer describeSerializer;
  private final ExecuteSerializer executeSerializer;
  private final FlushSerializer flushSerializer;
  private final ParseSerializer parseSerializer;
  private final QuerySerializer querySerializer;
  private final PasswordMessageSerializer passwordMessageSerializer;
  private final StartupMessageSerializer startupMessageSerializer;
  private final SyncSerializer syncSerializer;
  private final TerminateSerializer terminateSerializer;
  

  public Serializer(BindSerializer bindSerializer, CancelRequestSerializer cancelRequestSerializer,
      CloseSerializer closeSerializer, DescribeSerializer describeSerializer, ExecuteSerializer executeSerializer,
      FlushSerializer flushSerializer, ParseSerializer parseSerializer, QuerySerializer querySerializer,
      PasswordMessageSerializer passwordMessageSerializer, StartupMessageSerializer startupMessageSerializer,
      SyncSerializer syncSerializer, TerminateSerializer terminateSerializer) {
    super();
    this.bindSerializer = bindSerializer;
    this.cancelRequestSerializer = cancelRequestSerializer;
    this.closeSerializer = closeSerializer;
    this.describeSerializer = describeSerializer;
    this.executeSerializer = executeSerializer;
    this.flushSerializer = flushSerializer;
    this.parseSerializer = parseSerializer;
    this.querySerializer = querySerializer;
    this.passwordMessageSerializer = passwordMessageSerializer;
    this.startupMessageSerializer = startupMessageSerializer;
    this.syncSerializer = syncSerializer;
    this.terminateSerializer = terminateSerializer;
  }

  public final void encode(ClientMessage msg, BufferWriter b) {
    if (msg instanceof Bind)
      bindSerializer.encode((Bind) msg, b);
    else if (msg instanceof CancelRequest)
      cancelRequestSerializer.encode((CancelRequest) msg, b);
    else if (msg instanceof Close)
      closeSerializer.encode((Close) msg, b);
    else if (msg instanceof CopyData)
      notImplemented(CopyData.class);
    else if (msg instanceof CopyDone)
      notImplemented(CopyDone.class);
    else if (msg instanceof CopyFail)
      notImplemented(CopyFail.class);
    else if (msg instanceof Describe)
      describeSerializer.encode((Describe) msg, b);
    else if (msg instanceof Execute)
      executeSerializer.encode((Execute) msg, b);
    else if (msg instanceof Flush)
      flushSerializer.encode((Flush) msg, b);
    else if (msg instanceof FunctionCall)
      notImplemented(FunctionCall.class);
    else if (msg instanceof Parse)
      parseSerializer.encode((Parse) msg, b);
    else if (msg instanceof Query)
      querySerializer.encode((Query) msg, b);
    else if (msg instanceof PasswordMessage)
      passwordMessageSerializer.encode((PasswordMessage) msg, b);
    else if (msg instanceof SSLRequest)
      notImplemented(SSLRequest.class);
    else if (msg instanceof StartupMessage)
      startupMessageSerializer.encode((StartupMessage) msg, b);
    else if (msg instanceof Sync)
      syncSerializer.encode((Flush) msg, b);
    else if (msg instanceof Terminate)
      terminateSerializer.encode((Flush) msg, b);
    else
      log.severe("Invalid client message: " + msg);
  }

  private void notImplemented(Class<? extends ClientMessage> cls) {
    throw new UnsupportedOperationException("Decoder not implemented for class: " + cls);
  }
}
