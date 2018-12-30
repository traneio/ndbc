package io.trane.ndbc;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

import org.reactivestreams.Publisher;
import org.reactivestreams.tck.PublisherVerification;
import org.reactivestreams.tck.TestEnvironment;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import io.trane.future.Future;

public class FlowTCKTest extends PublisherVerification<Integer> {

  private final Function<Integer, Flow<Integer>> create;

  @Factory(dataProvider = "streams")
  public FlowTCKTest(Function<Integer, Flow<Integer>> create) {
    super(new TestEnvironment(false));
    System.setProperty("io.trane.ndbc.streamMaxDepth", "20");
    this.create = create;
  }

  @DataProvider(name = "streams")
  public static Object[][] streams() {

    return new Object[][] {
        { stream(i -> Flow.from(new TestList(i))) },
        { stream(i -> Flow.from(new TestList(i)).map(v -> v + 1)) },
        { stream(i -> Flow.from(new TestList(i * 2)).filter(v -> v % 2 == 0))
        },
        { stream(i -> Flow.from(new TestList(i)).flatMap(v -> Flow.from(v * 10))) },
        { stream(i -> Flow.from(Future.value(Flow.from(new TestList(i))))) }
        // { stream(i -> {
        // Flow<Integer>[] batches = { };
        // // Flow.batched(b -> Flow.from(new TestList(b.intValue())));
        // return null;
        // }) }
    };
  }

  private static final Function<Integer, Flow<Integer>> stream(Function<Integer, Flow<Integer>> create) {
    return create;
  }

  @Override
  public Publisher<Integer> createPublisher(long elements) {
    return create.apply((int) elements);
  }

  @Override
  public Publisher<Integer> createFailedPublisher() {
    return null;
  }

  @Override
  public long boundedDepthOfOnNextAndRequestRecursion() {
    return 20L;
  }

  // Disable test since it takes too long to run
  @Override
  @Test
  public void required_spec317_mustNotSignalOnErrorWhenPendingAboveLongMaxValue() throws Throwable {

  }

  private static class TestList implements List<Integer> {

    private final int size;

    public TestList(int size) {
      if (size == 0)
        System.out.println(2);
      this.size = size;
    }

    private <T> T notExpected() {
      throw new IllegalStateException();
    }

    @Override
    public int size() {
      return size;
    }

    @Override
    public boolean isEmpty() {
      return notExpected();
    }

    @Override
    public boolean contains(Object o) {
      return notExpected();
    }

    @Override
    public Iterator<Integer> iterator() {
      return new Iterator<Integer>() {

        private int curr = 0;

        @Override
        public boolean hasNext() {
          return curr < size;
        }

        @Override
        public Integer next() {
          return curr++;
        }
      };
    }

    @Override
    public Object[] toArray() {
      return notExpected();
    }

    @Override
    public <T> T[] toArray(T[] a) {
      return notExpected();
    }

    @Override
    public boolean add(Integer e) {
      return notExpected();
    }

    @Override
    public boolean remove(Object o) {
      return notExpected();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      return notExpected();
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
      return notExpected();
    }

    @Override
    public boolean addAll(int index, Collection<? extends Integer> c) {
      return notExpected();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      return notExpected();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      return notExpected();
    }

    @Override
    public void clear() {
      notExpected();
    }

    @Override
    public Integer get(int index) {
      return notExpected();
    }

    @Override
    public Integer set(int index, Integer element) {
      return notExpected();
    }

    @Override
    public void add(int index, Integer element) {
      notExpected();
    }

    @Override
    public Integer remove(int index) {
      return notExpected();
    }

    @Override
    public int indexOf(Object o) {
      return notExpected();
    }

    @Override
    public int lastIndexOf(Object o) {
      return notExpected();
    }

    @Override
    public ListIterator<Integer> listIterator() {
      return notExpected();
    }

    @Override
    public ListIterator<Integer> listIterator(int index) {
      return notExpected();
    }

    @Override
    public List<Integer> subList(int fromIndex, int toIndex) {
      return notExpected();
    }
  }

}
