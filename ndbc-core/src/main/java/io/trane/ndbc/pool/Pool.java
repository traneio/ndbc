package io.trane.ndbc.pool;

import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.future.Promise;

public interface Pool<T extends Pool.Item> {

  public interface Item {
    void release();

    boolean validate();
  }

  public static <T extends Pool.Item> Pool<T> apply(Supplier<Future<T>> supplier, int maxSize, int maxWaiters,
      Duration validationInterval) {
    return new BoundedPool<>(new CachedPool<>(supplier, validationInterval), maxSize, maxWaiters);
  }

  public <R> Future<R> apply(Function<T, Future<R>> f);
}

class CachedPool<T extends Pool.Item> implements Pool<T> {

  private final Supplier<Future<T>> supplier;
  private final ConcurrentLinkedQueue<T> items;
  private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

  public CachedPool(Supplier<Future<T>> supplier, Duration validationInterval) {
    super();
    this.supplier = supplier;
    this.items = new ConcurrentLinkedQueue<>();
    if (validationInterval.toMillis() != Long.MAX_VALUE)
      scheduleValidation(validationInterval);
  }

  @Override
  public <R> Future<R> apply(Function<T, Future<R>> f) {
    T item = items.poll();
    if (item != null)
      return f.apply(item);
    else
      return supplier.get().flatMap(i -> f.apply(i).ensure(() -> items.offer(i)));
  }

  private Future<Void> scheduleValidation(Duration validationInterval) {
    return Future.VOID.delayed(validationInterval.toMillis(), TimeUnit.MILLISECONDS, scheduler).flatMap(v -> {

      int size = items.size();

      long start = System.currentTimeMillis();

      for (int i = 0; i < size; i++) {
        T item = items.poll();
        if (item == null)
          break;
        else {
          boolean valid = false;
          try {
            valid = item.validate();
          } catch (Exception e) {
            valid = false;
            // TODO logging
          }
          if (!valid)
            try {
              item.release();
            } catch (Exception e) {
              // TODO logging
            }
          else
            items.offer(item);
        }
      }

      long next = validationInterval.toMillis() - System.currentTimeMillis() - start;

      if (next <= 0) {
        // TODO logging
        return scheduleValidation(validationInterval);
      } else
        return scheduleValidation(Duration.ofMillis(next));
    });
  }
}

class BoundedPool<T extends Pool.Item> implements Pool<T> {

  private static final Future<Object> POOL_EXHAUSTED = Future.exception(new RuntimeException("Pool exhausted"));

  private final Pool<T> underlying;
  private final Semaphore sizeSemaphore;
  private final Semaphore waitersSemaphore;
  private final Queue<Waiter<T, ?>> waiters;

  public BoundedPool(Pool<T> underlying, int maxSize, int maxWaiters) {
    super();
    this.underlying = underlying;
    this.sizeSemaphore = semaphore(maxSize);
    this.waitersSemaphore = semaphore(maxWaiters);
    this.waiters = new ConcurrentLinkedQueue<>();
  }

  @Override
  public <R> Future<R> apply(Function<T, Future<R>> f) {
    if (sizeSemaphore.tryAcquire())
      return underlying.apply(f).ensure(() -> releaseOne());
    else if (waitersSemaphore.tryAcquire()) {
      Waiter<T, R> p = new Waiter<>(f);
      waiters.add(p);
      return p;
    } else
      return POOL_EXHAUSTED.unsafeCast();
  }

  private static class Waiter<T, R> extends Promise<R> {

    private final Function<T, Future<R>> f;

    public Waiter(Function<T, Future<R>> f) {
      super();
      this.f = f;
    }

    public Waiter<T, R> apply(T value) {
      become(f.apply(value));
      return this;
    }
  }

  private final void releaseOne() {
    Waiter<T, ?> waiter = waiters.poll();
    if (waiter != null) {
      waitersSemaphore.release();
      underlying.apply(waiter::apply).ensure(() -> releaseOne());
    } else
      sizeSemaphore.release();
  };

  private Semaphore semaphore(int permits) {
    if (permits == Integer.MAX_VALUE)
      return new Semaphore(permits) {
        private static final long serialVersionUID = 1L;

        @Override
        public void release() {
        }

        @Override
        public boolean tryAcquire() {
          return true;
        }

      };
    else
      return new Semaphore(permits);
  }
}
