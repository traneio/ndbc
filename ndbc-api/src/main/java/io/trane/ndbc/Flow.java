package io.trane.ndbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.trane.future.Future;
import io.trane.future.Promise;

/**
 * A composable TCK-compliant reactive streams `Publisher` implementation.
 * 
 * @param <T>
 *          the stream element type
 */
public class Flow<T> implements Publisher<T> {

  public static final class Collect<T> {

    private final Flow<T> flow;
    private final long    requestSize;

    private Collect(Flow<T> flow, long requestSize) {
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

  private final Consumer<Subscriber<? super T>> subscribe;

  public Flow(Consumer<Subscriber<? super T>> subscribe) {
    this.subscribe = subscribe;
  }

  public static final <U> Flow<U> from(Publisher<U> publisher) {
    return new Flow<U>(publisher::subscribe);
  }

  @SafeVarargs
  public static final <U> Flow<U> from(U... list) {
    return from(Arrays.asList(list));
  }

  private static final class Scheduler {

    private static final int maxDepth = Optional
        .ofNullable(System.getProperty("io.trane.ndbc.maxStackDepth")).map(Integer::parseInt).orElse(512);

    private static final ThreadLocal<Scheduler> current = new ThreadLocal<Scheduler>() {
      @Override
      protected Scheduler initialValue() {
        return new Scheduler();
      }
    };

    public static void submit(Runnable r) {
      current.get().run(r);
    }

    private boolean        running = false;
    private List<Runnable> pending;
    private int            depth   = 0;

    private final void run(Runnable r) {
      if (++depth < maxDepth) {
        r.run();
        depth--;
      } else {
        pending = new ArrayList<>();
        pending.add(r);
        if (!running) {
          running = true;
          while (pending != null) {
            List<Runnable> p = pending;
            pending = null;
            for (int i = 0; i < p.size(); i++)
              p.get(i).run();
          }
          depth = maxDepth;
          running = false;
        }
      }
    }
  }

  public static final <U> Flow<U> batched(Function<Long, Optional<Flow<U>>> fetch) {
    return new Flow<U>(sb -> {
      sb.onSubscribe(new Subscription() {

        @Override
        public void request(long n) {
          Optional<Flow<U>> f = fetch.apply(n);
          if (f.isPresent())
            f.get().subscribe(new Subscriber<U>() {

              private final AtomicLong pending = new AtomicLong(n);

              @Override
              public void onSubscribe(Subscription s) {
                s.request(n);
              }

              @Override
              public void onNext(U t) {
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
    });
  }

  public static final <U> Flow<U> from(Future<Flow<U>> fut) {
    return new Flow<>(sb -> {

      Future<Subscription> subscription = fut.flatMap(st -> {
        Promise<Subscription> p = Promise.apply();
        st.subscribe(new Subscriber<U>() {

          @Override
          public void onSubscribe(Subscription s) {
            p.setValue(s);
          }

          @Override
          public void onNext(U t) {
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
    });
  }

  public static final <U> Flow<U> from(List<U> list) {
    return new Flow<>(s -> {
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
                list.stream().skip(start).limit(n).forEach(v -> {
                  System.out.println(1);
                  s.onNext(v);
                });
                if (start + n >= list.size()) {
                  idx.set(-1);
                  s.onComplete();
                }
              }
            }
          });
        }

        @Override
        public void cancel() {
          idx.set(-1);
        }
      });
    });
  }

  @Override
  public final void subscribe(Subscriber<? super T> s) {
    subscribe.accept(s);
  }

  public Collect<T> collect(long requestSize) {
    return new Collect<>(this, requestSize);
  }

  public Flow<T> filter(Predicate<T> p) {
    return new Flow<>(sb -> {
      subscribe.accept(new Subscriber<T>() {

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
    });
  }

  public <U> Flow<U> map(Function<T, U> f) {
    return new Flow<>(sb -> {
      subscribe.accept(new Subscriber<T>() {
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
    });
  }

  public <U> Flow<U> flatMap(Function<T, Flow<U>> f) {
    return new Flow<U>(sb -> {
      subscribe.accept(new Subscriber<T>() {

        Deque<Subscription> subscriptions = new ConcurrentLinkedDeque<>();
        AtomicLong          lastRequest   = new AtomicLong(0L);
        AtomicLong          pending       = new AtomicLong(0L);

        Subscription coord = new Subscription() {
          @Override
          public void request(final long n) {
            Scheduler.submit(() -> {
              if (pending.get() >= 0) {
                if (subscriptions.isEmpty()) {
                  pending.set(-1);
                  sb.onComplete();
                } else {
                  pending.set(n);
                  subscriptions.peekFirst().request(n);
                }
              }
            });
          }

          @Override
          public void cancel() {
            subscriptions.forEach(s -> s.cancel());
          }
        };

        @Override
        public void onSubscribe(Subscription s) {
          subscriptions.add(s);
          sb.onSubscribe(new Subscription() {

            @Override
            public void request(long n) {
              lastRequest.set(n);
              coord.request(n);
            }

            @Override
            public void cancel() {
              coord.cancel();
            }
          });
        }

        @Override
        public void onNext(T t) {
          f.apply(t).subscribe(new Subscriber<U>() {

            private Subscription s = null;

            @Override
            public void onSubscribe(Subscription s) {
              this.s = s;
              Subscription prevLast = subscriptions.removeLast();
              subscriptions.addLast(s);
              subscriptions.addLast(prevLast);
            }

            @Override
            public void onNext(U t) {
              sb.onNext(t);
              pending.decrementAndGet();
            }

            @Override
            public void onError(Throwable t) {
              subscriptions.forEach(s -> s.cancel());
              sb.onError(t);
            }

            @Override
            public void onComplete() {
              subscriptions.removeFirstOccurrence(s);
              long p = pending.get();
              if (p > 0)
                coord.request(p);
            }
          });
          long p = pending.decrementAndGet();
          if (p == 0L)
            coord.request(lastRequest.get());
        }

        @Override
        public void onError(Throwable t) {
          subscriptions.forEach(s -> s.cancel());
          sb.onError(t);
        }

        @Override
        public void onComplete() {
          subscriptions.removeLast();
          coord.request(lastRequest.get());
        }
      });
    });
  }
}
