package io.trane.ndbc.tck;

import static java.lang.Math.min;

import java.util.concurrent.atomic.AtomicLong;

import org.reactivestreams.Publisher;

import io.trane.ndbc.flow.Flow;

public class BatchedTCKTest extends TCKTest {

  @Override
  public Publisher<Integer> createPublisher(long elements) {
    AtomicLong pending = new AtomicLong(elements);
    return Flow.batched(i -> {
      long remaining = pending.getAndUpdate(j -> j - i);
      if (remaining <= 0L)
        return Flow.empty();
      else {
        int size = (int) min(remaining, i);
        return Flow.from(list(size));
      }
    });
  }
}
