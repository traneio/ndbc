package io.trane.ndbc.datasource;

import java.time.Duration;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.function.Function;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.future.Promise;
import io.trane.ndbc.Connection;

public final class Pool<T extends Connection> {

  private static final Future<Object> POOL_EXHAUSTED = Future.exception(new RuntimeException("Pool exhausted"));
  private static final Future<Object> POOL_CLOSED    = Future.exception(new RuntimeException("Pool closed"));

  public static <T extends Connection> Pool<T> apply(final Supplier<Future<T>> supplier,
      final Optional<Integer> maxSize, final Optional<Integer> maxWaiters, final Optional<Duration> validationInterval,
      final ScheduledExecutorService scheduler) {
    return new Pool<>(supplier, maxSize, maxWaiters, validationInterval, scheduler);
  }

  private volatile boolean          closed = false;
  private final Supplier<Future<T>> supplier;
  private final Semaphore           sizeSemaphore;
  private final Semaphore           waitersSemaphore;
  private final Queue<T>            items;
  private final Queue<Waiter<T, ?>> waiters;

  private Pool(final Supplier<Future<T>> supplier, final Optional<Integer> maxSize, final Optional<Integer> maxWaiters,
      final Optional<Duration> validationInterval, final ScheduledExecutorService scheduler) {
    this.supplier = supplier;
    this.sizeSemaphore = semaphore(maxSize);
    this.waitersSemaphore = semaphore(maxWaiters);
    // TODO is this the best data structure?
    this.items = new ConcurrentLinkedQueue<>();
    this.waiters = new ConcurrentLinkedQueue<>();
    validationInterval.ifPresent(i -> scheduleValidation(i, scheduler));
  }

  public final <R> Future<R> apply(final Function<T, Future<R>> f) {
    if (closed)
      return POOL_CLOSED.unsafeCast();
    else {
      final T item = items.poll();
      if (item != null)
        return Future.flatApply(() -> f.apply(item)).ensure(() -> release(item));
      else if (sizeSemaphore.tryAcquire())
        return supplier.get().flatMap(i -> f.apply(i).ensure(() -> release(i)));
      else if (waitersSemaphore.tryAcquire()) {
        final Waiter<T, R> p = new Waiter<>(f);
        waiters.offer(p);
        return p;
      } else
        return POOL_EXHAUSTED.unsafeCast();
    }
  }

  public final Future<Void> close() {
    closed = true;

    Waiter<?, ?> w;
    while ((w = waiters.poll()) != null) {
      waitersSemaphore.release();
      w.become(POOL_CLOSED.unsafeCast());
    }

    return drain();
  }

  private final Future<Void> drain() {
    final T item = items.poll();
    if (item == null)
      return Future.VOID;
    else
      return item.close().flatMap(v -> drain());
  }

  private final void release(final T item) {
    if (closed)
      item.close();
    else {
      final Waiter<T, ?> waiter = waiters.poll();
      if (waiter != null) {
        waitersSemaphore.release();
        waiter.apply(item).ensure(() -> release(item));
      } else
        items.offer(item);
    }
  }

  private final Future<Void> validateN(final int n) {
    if (n >= 0) {
      final T item = items.poll();
      if (item == null) {
        return Future.VOID;
      } else
        // TODO logging
        return item.isValid().rescue(e -> Future.FALSE).flatMap(valid -> {
          if (!valid)
            return item.close().rescue(e -> Future.VOID).ensure(() -> sizeSemaphore.release());
          else {
            items.offer(item);
            return Future.VOID;
          }
        }).flatMap(v -> validateN(n - 1));
    } else
      return Future.VOID;
  }

  private final Future<Void> scheduleValidation(final Duration validationInterval,
      final ScheduledExecutorService scheduler) {
    return Future.VOID.delayed(validationInterval, scheduler).flatMap(v1 -> {
      final long start = System.currentTimeMillis();
      return validateN(items.size()).flatMap(v2 -> {
        final long next = validationInterval.toMillis() - System.currentTimeMillis() - start;
        if (next <= 0) {
          // TODO logging
          return scheduleValidation(validationInterval, scheduler);
        } else
          return scheduleValidation(Duration.ofMillis(next), scheduler);
      });
    });
  }

  private static final class Waiter<T, R> extends Promise<R> {

    private final Function<T, Future<R>> f;

    public Waiter(final Function<T, Future<R>> f) {
      super();
      this.f = f;
    }

    public Waiter<T, R> apply(final T value) {
      become(f.apply(value));
      return this;
    }
  }

  private final Semaphore semaphore(final Optional<Integer> permits) {
    return permits.map(Semaphore::new).orElse(new Semaphore(Integer.MAX_VALUE) {
      private static final long serialVersionUID = 1L;

      @Override
      public void release() {
      }

      @Override
      public boolean tryAcquire() {
        return true;
      }
    });
  }
}
