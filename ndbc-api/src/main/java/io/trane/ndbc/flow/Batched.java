package io.trane.ndbc.flow;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

final class Batched<T> implements Flow<T> {

  private final Function<Long, Flow<T>> fetch;

  public Batched(Function<Long, Flow<T>> fetch) {
    this.fetch = fetch;
  }

  @Override
  public void subscribe(Subscriber<? super T> sb) {
    sb.onSubscribe(new Subscription() {

      private final AtomicBoolean done = new AtomicBoolean(false);

      @Override
      public void request(long n) {

        if (n <= 0L)
          sb.onError(new IllegalArgumentException("Invalid request: " + n));

        else if (!done.get()) {

          Flow<T> f = fetch.apply(n);
          f.subscribe(new Subscriber<T>() {

            private final AtomicLong pending = new AtomicLong(n);

            @Override
            public void onSubscribe(Subscription s) {
              s.request(n);
            }

            @Override
            public void onNext(T t) {
              sb.onNext(t);
              pending.decrementAndGet();
            }

            @Override
            public void onError(Throwable t) {
              sb.onError(t);
            }

            @Override
            public void onComplete() {
              if (pending.get() > 0) {
                sb.onComplete();
                done.set(true);
              }
            }
          });
        }
      }

      @Override
      public void cancel() {
        done.set(true);
      }
    });
  }
}
