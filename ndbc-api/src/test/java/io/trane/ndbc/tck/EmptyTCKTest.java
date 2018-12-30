package io.trane.ndbc.tck;

import org.reactivestreams.Publisher;

import io.trane.ndbc.flow.Flow;

public class EmptyTCKTest extends TCKTest {

  @Override
  public Publisher<Integer> createPublisher(long elements) {
    return elements == 0L ? Flow.empty() : Flow.from(list((int) elements));
  }
}
