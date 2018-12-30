package io.trane.ndbc.tck;

import org.reactivestreams.Publisher;

import io.trane.ndbc.flow.Flow;

public class FromListTCKTest extends TCKTest {

  @Override
  public Publisher<Integer> createPublisher(long elements) {
    return Flow.from(list((int) elements)).map(i -> i + 1);
  }
}
