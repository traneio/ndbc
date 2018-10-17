package io.trane.ndbc.netty4;

import java.util.concurrent.CancellationException;

import io.netty.channel.ChannelFuture;
import io.trane.future.Future;
import io.trane.future.Promise;

public interface ChannelFutureHandler {

  public static Future<Void> toFuture(final ChannelFuture cf) {
    if (cf.isDone())
      return Future.VOID;
    else {
      final Promise<Void> p = Promise.apply();
      cf.addListener(fut -> {
        assert fut.isDone();
        if (fut.isCancelled())
          p.raise(new CancellationException());
        else if (fut.isSuccess())
          p.setValue(null);
        else
          p.setException(fut.cause());
      });
      return p;
    }
  }
}
