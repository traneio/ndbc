package io.trane.ndbc.flow;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

final class Batched<T> implements Flow<T> {

  private final Function<Long, Optional<Flow<T>>> fetch;

  public Batched(Function<Long, Optional<Flow<T>>> fetch) {
    this.fetch = fetch;
  }

  @Override
  public void subscribe(Subscriber<? super T> sb) {
    sb.onSubscribe(new Subscription() {

      @Override
      public void request(long n) {
        Optional<Flow<T>> f = fetch.apply(n);
        if (f.isPresent())
          f.get().subscribe(new Subscriber<T>() {

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
              if (pending.get() != 0)
                sb.onError(
                    new IllegalStateException("Fetch function returned a stream with less elements than requested."));
            }
          });
        else
          sb.onComplete();
      }

      @Override
      public void cancel() {
      }
    });
  }
}
