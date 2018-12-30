package io.trane.ndbc.flow;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.trane.future.Future;
import io.trane.future.Promise;

final class FromFuture<T> implements Flow<T> {

  private final Future<Flow<T>> fut;

  public FromFuture(Future<Flow<T>> fut) {
    this.fut = fut;
  }

  @Override
  public void subscribe(Subscriber<? super T> sb) {
    Future<Subscription> subscription = fut.flatMap(st -> {
      Promise<Subscription> p = Promise.apply();
      st.subscribe(new Subscriber<T>() {

        @Override
        public void onSubscribe(Subscription s) {
          p.setValue(s);
        }

        @Override
        public void onNext(T t) {
          sb.onNext(t);
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
      return p;
    });

    sb.onSubscribe(new Subscription() {

      @Override
      public void request(long n) {
        subscription.onSuccess(v -> v.request(n));
      }

      @Override
      public void cancel() {
        subscription.onSuccess(v -> v.cancel());
      }
    });
  }
}