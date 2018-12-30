package io.trane.ndbc.flow;

import java.util.function.Function;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

final class Map<T, U> implements Flow<U> {

  private final Flow<T>        flow;
  private final Function<T, U> f;

  public Map(Flow<T> flow, Function<T, U> f) {
    this.flow = flow;
    this.f = f;
  }

  @Override
  public void subscribe(Subscriber<? super U> sb) {
    flow.subscribe(new Subscriber<T>() {
      @Override
      public void onSubscribe(Subscription s) {
        sb.onSubscribe(s);
      }

      @Override
      public void onNext(T t) {
        sb.onNext(f.apply(t));
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
