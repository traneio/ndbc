package io.trane.ndbc.flow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.trane.future.Future;
import io.trane.future.Promise;

final class Collect<T> {

  private final Flow<T> flow;
  private final long    requestSize;

  public Collect(Flow<T> flow, long requestSize) {
    this.flow = flow;
    this.requestSize = requestSize;
  }

  public Future<List<T>> toList() {
    ArrayList<T> unsynchronized = new ArrayList<>();
    List<T> results = Collections.synchronizedList(unsynchronized);
    return foreach(results::add).map(v -> unsynchronized);
  }

  public Future<Void> foreach(Consumer<T> c) {
    Promise<Void> p = Promise.apply();
    flow.subscribe(new Subscriber<T>() {

      AtomicLong   pending = new AtomicLong();
      Subscription s       = null;

      @Override
      public void onSubscribe(Subscription s) {
        this.s = s;
        nextBatch(s);
      }

      private void nextBatch(Subscription s) {
        pending.set(requestSize);
        s.request(requestSize);
      }

      @Override
      public void onNext(T t) {
        long p = pending.decrementAndGet();
        c.accept(t);
        if (p == 0)
          nextBatch(s);
      }

      @Override
      public void onError(Throwable t) {
        pending.set(-1);
        p.setException(t);
      }

      @Override
      public void onComplete() {
        pending.set(-1);
        p.become(Future.VOID);
      }
    });
    return p;
  }
}