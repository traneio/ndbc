package io.trane.ndbc.datasource;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Concurrently {

  public static void apply(final Duration howLong, final Runnable run, final Runnable checkInvariants) {
    final int threads = Runtime.getRuntime().availableProcessors() * 2;
    final ExecutorService executor = Executors.newFixedThreadPool(threads);
    final ExecutorService validator = Executors.newFixedThreadPool(1);
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    final AtomicBoolean stop = new AtomicBoolean(false);
    final AtomicReference<Throwable> error = new AtomicReference<>();
    try {

      validator.submit(() -> {
        while (!stop.get())
          try {
            checkInvariants.run();
          } catch (final Throwable e) {
            error.set(e);
            stop.set(true);
          }
      });

      scheduler.schedule(() -> stop.set(true), howLong.toMillis(), TimeUnit.MILLISECONDS);

      while (!stop.get())
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
