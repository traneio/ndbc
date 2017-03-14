package io.trane.ndbc.pool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Concurrently {

  public static void apply(int threads, int times, Runnable run) {
    apply(threads, times, run, () -> {
    });
  }

  public static void apply(int threads, int times, Runnable run, Runnable checkInvariants) {
    ExecutorService executor = Executors.newFixedThreadPool(threads);
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    CountDownLatch start = new CountDownLatch(1);
    CountDownLatch done = new CountDownLatch(times);
    AtomicReference<RuntimeException> error = new AtomicReference<>();
    try {

      scheduler.scheduleAtFixedRate(() -> {
        try {
          checkInvariants.run();
        } catch (RuntimeException e) {
          error.set(e);
        }
      }, 1, 5, TimeUnit.MILLISECONDS);

      for (int i = 0; i < times; i++) {
        executor.submit(() -> {
          try {
            start.await();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
          run.run();
          done.countDown();
        });
      }

      try {
        start.countDown();
        done.await();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      if (error.get() != null)
        throw error.get();
    } finally {
      executor.shutdown();
      scheduler.shutdown();
    }
  }
}
