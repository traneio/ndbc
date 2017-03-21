package io.trane.ndbc.pool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import io.trane.future.Future;
import io.trane.future.Promise;

public class PoolTest {

  @Test
  public void maxSize() {
    int maxSize = 100;
    Pool<Integer> pool = Pool.apply(() -> Future.value(1), i -> Future.VOID, i -> Future.TRUE, maxSize,
        Integer.MAX_VALUE, Duration.ofMillis(Long.MAX_VALUE));
    AtomicInteger executing = new AtomicInteger();

    for (int i = 0; i < maxSize * 3; i++)
      pool.apply(t -> {
        executing.incrementAndGet();
        return Promise.apply();
      });

    assertEquals(maxSize, executing.get());
  }

  @Test
  public void maxSizeConcurrentCreation() {
    int maxSize = 100;
    Pool<Integer> pool = Pool.apply(() -> Future.value(1), i -> Future.VOID, i -> Future.TRUE, maxSize,
        Integer.MAX_VALUE, Duration.ofMillis(Long.MAX_VALUE));
    AtomicInteger executing = new AtomicInteger();

    Concurrently.apply(Duration.ofMillis(200), () -> {
      pool.apply(t -> {
        executing.incrementAndGet();
        return Promise.apply();
      });
    }, () -> {
      assertTrue(maxSize >= executing.get());
    });
  }

  @Test
  public void maxSizeConcurrentUsage() {
    int maxSize = 100;
    Pool<Integer> pool = Pool.apply(() -> Future.value(1), i -> Future.VOID, i -> Future.TRUE, maxSize,
        Integer.MAX_VALUE, Duration.ofMillis(Long.MAX_VALUE));
    AtomicInteger executing = new AtomicInteger();
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    Concurrently.apply(Duration.ofMillis(200), () -> {
      pool.apply(t -> {
        executing.incrementAndGet();
        return Future.delay(Duration.ofMillis(1), scheduler).ensure(() -> executing.decrementAndGet());
      });
    }, () -> {
      assertTrue(maxSize >= executing.get());
    });
  }

  @Test
  public void maxWaiters() {
    int maxSize = 100;
    int maxWaiters = 60;
    Pool<Integer> pool = Pool.apply(() -> Future.value(1), i -> Future.VOID, i -> Future.TRUE, maxSize, maxWaiters,
        Duration.ofMillis(Long.MAX_VALUE));
    AtomicInteger executing = new AtomicInteger();
    AtomicInteger rejected = new AtomicInteger();

    for (int i = 0; i < 200; i++)
      pool.apply(t -> {
        executing.incrementAndGet();
        return Promise.apply();
      }).onFailure(e -> rejected.incrementAndGet());

    assertEquals(maxSize, executing.get());
    assertEquals(40, rejected.get());
  }

  @Test
  public void maxWaitersConcurrentCreation() {
    int maxSize = 100;
    int maxWaiters = 60;
    Pool<Integer> pool = Pool.apply(() -> Future.value(1), i -> Future.VOID, i -> Future.TRUE, maxSize, maxWaiters,
        Duration.ofMillis(Long.MAX_VALUE));
    AtomicInteger started = new AtomicInteger();
    AtomicInteger executing = new AtomicInteger();
    AtomicInteger rejected = new AtomicInteger();

    Concurrently.apply(Duration.ofMillis(200), () -> {
      started.incrementAndGet();
      pool.apply(t -> {
        executing.incrementAndGet();
        return Promise.apply();
      }).onFailure(e -> rejected.incrementAndGet());
    }, () -> {
      assertTrue(maxSize >= executing.get());
      System.out.println(executing.get());
    });
  }

}