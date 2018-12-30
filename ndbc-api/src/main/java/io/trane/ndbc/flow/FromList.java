package io.trane.ndbc.flow;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

final class FromList<T> implements Flow<T> {

  private final List<T> list;

  public FromList(List<T> list) {
    this.list = list;
  }

  @Override
  public void subscribe(Subscriber<? super T> s) {
    s.onSubscribe(new Subscription() {

      private AtomicInteger idx = new AtomicInteger(0);

      @Override
      public void request(final long l) {
        Scheduler.submit(() -> {

          final int n;
          if (l > Integer.MAX_VALUE)
            n = Integer.MAX_VALUE;
          else
            n = (int) l;

          if (n <= 0)
            s.onError(new IllegalArgumentException("Invalid request: " + n));
          else {
            int currIdx = idx.get();
            if (currIdx >= 0) {
              int start = idx.getAndUpdate(i -> i + n);
              boolean done = start + n >= list.size();

              if (done)
                idx.set(-1);

              list.stream().skip(start).limit(n).forEach(s::onNext);

              if (done)
                s.onComplete();
            }
          }
        });
      }

      @Override
      public void cancel() {
        idx.set(-1);
      }
    });
  }

}
