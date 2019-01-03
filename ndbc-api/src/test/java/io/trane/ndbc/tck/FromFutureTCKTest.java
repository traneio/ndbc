package io.trane.ndbc.tck;

import org.reactivestreams.Publisher;

import io.trane.future.Future;
import io.trane.ndbc.flow.Flow;

public class FromFutureTCKTest extends TCKTest {

  @Override
  public Publisher<Integer> createPublisher(long elements) {
    return Flow.from(Future.value(Flow.from(list((int) elements))));
  }
}
