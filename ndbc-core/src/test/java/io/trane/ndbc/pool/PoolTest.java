package io.trane.ndbc.pool;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
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
    Pool<TestItem> pool = Pool.apply(() -> Future.value(new TestItem()), maxSize, Integer.MAX_VALUE,
        Duration.ofMillis(Long.MAX_VALUE));
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
    Pool<TestItem> pool = Pool.apply(() -> Future.value(new TestItem()), maxSize, Integer.MAX_VALUE,
        Duration.ofMillis(Long.MAX_VALUE));
    AtomicInteger executing = new AtomicInteger();

    Concurrently.apply(200, 10000, () -> {
      pool.apply(t -> {
        executing.incrementAndGet();
        return Promise.apply();
      });
    });

    assertEquals(maxSize, executing.get());
  }

  @Test
  public void maxSizeConcurrentUsage() {
    int maxSize = 100;
    Pool<TestItem> pool = Pool.apply(() -> Future.value(new TestItem()), maxSize, Integer.MAX_VALUE,
        Duration.ofMillis(Long.MAX_VALUE));
    AtomicInteger executing = new AtomicInteger();
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    Concurrently.apply(200, 10000, () -> {
      pool.apply(t -> {
        executing.incrementAndGet();
        return Future.delay(Duration.ofMillis(1), scheduler).ensure(() -> executing.decrementAndGet());
      });
    }, () -> {
      System.out.println("sdds");
      assertEquals(maxSize, executing.get());
    });
  }

}

class TestItem implements Pool.Item {
  @Override
  public Future<Void> release() {
    return Future.VOID;
  }

  @Override
  public Future<Boolean> validate() {
    return Future.TRUE;
  }
}