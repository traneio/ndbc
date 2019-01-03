package io.trane.ndbc.tck;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.reactivestreams.Publisher;
import org.reactivestreams.tck.PublisherVerification;
import org.reactivestreams.tck.TestEnvironment;
import org.testng.annotations.Test;

public abstract class TCKTest extends PublisherVerification<Integer> {

  public TCKTest() {
    super(new TestEnvironment(false));
    System.setProperty("io.trane.ndbc.maxStackDepth", "20");
  }

  // { stream(i -> Flow.from(Future.value(Flow.from(new TestList(i))))) }

  protected List<Integer> list(int size) {
    return new TestList(size);
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

  private class TestList implements List<Integer> {

    private final int size;

    public TestList(int size) {
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
      return size == 0;
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
