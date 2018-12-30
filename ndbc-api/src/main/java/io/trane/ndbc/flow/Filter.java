package io.trane.ndbc.flow;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

final class Filter<T> implements Flow<T> {

  private final Flow<T>      flow;
  private final Predicate<T> p;

  public Filter(Flow<T> flow, Predicate<T> p) {
    this.flow = flow;
    this.p = p;
  }

  @Override
  public void subscribe(Subscriber<? super T> sb) {

    flow.subscribe(new Subscriber<T>() {

      private final AtomicLong filteredOut = new AtomicLong(0);
      private final AtomicLong pending     = new AtomicLong(0);

      private Subscription s = null;

      @Override
      public void onSubscribe(Subscription s) {
        this.s = new Subscription() {

          @Override
          public void request(long n) {
            Scheduler.submit(() -> {
              filteredOut.set(0);
              pending.set(n);
              s.request(n);
            });
          }

          @Override
          public void cancel() {
            s.cancel();
          }
        };
        sb.onSubscribe(this.s);
      }

      @Override
      public void onNext(T t) {
        if (p.test(t))
          sb.onNext(t);
        else
          filteredOut.incrementAndGet();
        long p = pending.decrementAndGet();
        if (p == 0L) {
          long f = filteredOut.get();
          if (f > 0)
            s.request(f);
        }
      }

      @Override
      public void onError(Throwable t) {
        sb.onError(t);
      }

      @Override
      public void onComplete() {
        sb.onComplete();
      }
    });
  }

}
