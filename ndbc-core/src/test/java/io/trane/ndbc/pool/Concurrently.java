package io.trane.ndbc.pool;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Concurrently {

  public static void apply(Duration howLong, Runnable run, Runnable checkInvariants) {
    int threads = Runtime.getRuntime().availableProcessors() * 2;
    ExecutorService executor = Executors.newFixedThreadPool(threads);
    ExecutorService validator = Executors.newFixedThreadPool(1);
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    AtomicBoolean stop = new AtomicBoolean(false);
    AtomicReference<Throwable> error = new AtomicReference<>();
    try {

      validator.submit(() -> {
        while (!stop.get())
          try {
            checkInvariants.run();
          } catch (Throwable e) {
            error.set(e);
            stop.set(true);
          }
      });
      
      scheduler.schedule(() -> stop.set(true), howLong.toMillis(), TimeUnit.MILLISECONDS);

      while(!stop.get())
        executor.submit(run);

      executor.shutdownNow();

      if (error.get() != null)
        throw new RuntimeException(error.get());

    } finally {
      executor.shutdown();
      validator.shutdown();
      scheduler.shutdown();
    }
  }
}
