package io.trane.ndbc.flow;

import java.util.concurrent.atomic.AtomicBoolean;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

final class Empty<T> implements Flow<T> {

  @Override
  public void subscribe(Subscriber<? super T> sb) {
    sb.onSubscribe(new Subscription() {

      private final AtomicBoolean done = new AtomicBoolean(false);

      @Override
      public void request(final long l) {
        if (done.compareAndSet(false, true))
          sb.onComplete();
      }

      @Override
      public void cancel() {
      }
    });
  }

}
