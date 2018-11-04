package io.trane.ndbc.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.future.Future;
import io.trane.future.Promise;
import io.trane.future.TimeoutException;

public class LockFreePoolTest extends PoolEnv {

  @Test
  public void maxSize() {
    final int maxSize = 100;
    final Pool<Connection> pool = LockFreePool.create(() -> Future.value(conn()),
        Optional.of(maxSize),
        Optional.empty(), Optional.empty(), Optional.empty(), scheduler);
    final AtomicInteger executing = new AtomicInteger();

    for (int i = 0; i < (maxSize * 3); i++)
      pool.acquire().flatMap(t -> {
        executing.incrementAndGet();
        return Promise.apply();
      });

    assertEquals(maxSize, executing.get());
  }

  @Test
  public void maxSizeConcurrentCreation() {
    final int maxSize = 100;
    final Pool<Connection> pool = LockFreePool.create(() -> Future.value(conn()),
        Optional.of(maxSize),
        Optional.empty(), Optional.empty(), Optional.empty(), scheduler);
    final AtomicInteger executing = new AtomicInteger();

    Concurrently.apply(Duration.ofMillis(200), () -> {
      pool.acquire().flatMap(t -> {
        executing.incrementAndGet();
        return Promise.apply();
      });
    }, () -> {
      assertTrue(maxSize >= executing.get());
    });
  }

  @Test
  public void maxSizeConcurrentUsage() {
    final int maxSize = 100;
    final Pool<Connection> pool = LockFreePool.create(() -> Future.value(conn()),
        Optional.of(maxSize),
        Optional.empty(), Optional.empty(), Optional.empty(), scheduler);
    final AtomicInteger executing = new AtomicInteger();
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    Concurrently.apply(Duration.ofMillis(200), () -> {
      pool.acquire().flatMap(t -> {
        executing.incrementAndGet();
        return Future.delay(Duration.ofMillis(1), scheduler).ensure(() -> {
          executing.decrementAndGet();
          pool.release(t);
        });
      });
    }, () -> {
      assertTrue(maxSize >= executing.get());
    });
  }

  @Test
  public void maxWaiters() {
    final int maxSize = 100;
    final int maxWaiters = 60;
    final Pool<Connection> pool = LockFreePool.create(() -> Future.value(conn()),
        Optional.of(maxSize),
        Optional.of(maxWaiters), Optional.empty(), Optional.empty(), scheduler);
    final AtomicInteger executing = new AtomicInteger();
    final AtomicInteger rejected = new AtomicInteger();

    for (int i = 0; i < 200; i++)
      pool.acquire().flatMap(t -> {
        executing.incrementAndGet();
        return Promise.apply();
      }).onFailure(e -> rejected.incrementAndGet());

    assertEquals(maxSize, executing.get());
    assertEquals(40, rejected.get());
  }

  @Test
  public void maxWaitersConcurrentCreation() {
    final int maxSize = 100;
    final int maxWaiters = 60;
    final Pool<Connection> pool = LockFreePool.create(() -> Future.value(conn()),
        Optional.of(maxSize),
        Optional.of(maxWaiters), Optional.empty(), Optional.empty(), scheduler);
    final AtomicInteger started = new AtomicInteger();
    final AtomicInteger executing = new AtomicInteger();
    final AtomicInteger rejected = new AtomicInteger();

    Concurrently.apply(Duration.ofMillis(200), () -> {
      started.incrementAndGet();
      pool.acquire().flatMap(t -> {
        executing.incrementAndGet();
        return Promise.apply();
      }).onFailure(e -> rejected.incrementAndGet());
    }, () -> {
      assertTrue(maxSize >= executing.get());
    });
  }

  @Test(expected = TimeoutException.class)
  public void connectionTimeout() throws CheckedFutureException {
    final Pool<Connection> pool = LockFreePool.create(
        () -> Future.value(conn()).delayed(Duration.ofMillis(100), scheduler),
        Optional.empty(), Optional.empty(), Optional.of(Duration.ofMillis(10)),
        Optional.empty(), scheduler);

    pool.acquire().get(Duration.ofMillis(200));
  }
}