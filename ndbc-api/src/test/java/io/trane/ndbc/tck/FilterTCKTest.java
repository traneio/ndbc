package io.trane.ndbc.tck;

import org.reactivestreams.Publisher;

import io.trane.ndbc.flow.Flow;

public class FilterTCKTest extends TCKTest {

  @Override
  public Publisher<Integer> createPublisher(long elements) {
    return Flow.from(list((int) elements * 2)).filter(v -> v % 2 == 0);
  }
}
