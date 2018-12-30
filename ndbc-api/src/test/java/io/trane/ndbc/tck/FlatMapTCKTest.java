package io.trane.ndbc.tck;

import org.reactivestreams.Publisher;

import io.trane.ndbc.flow.Flow;

public class FlatMapTCKTest extends TCKTest {

  @Override
  public Publisher<Integer> createPublisher(long elements) {
    return Flow.from(list((int) elements * 2)).flatMap(v -> v % 2 == 0 ? Flow.empty() : Flow.from(v * 10));
  }
}
