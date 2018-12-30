package io.trane.ndbc.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class Scheduler {

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