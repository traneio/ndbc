package io.trane.ndbc.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Try<T> {

  public static <T> Try<T> apply(Supplier<T> supplier) {
    try {
      return new Success<T>(supplier.get());
    } catch (Exception t) {
      NonFatalException.verify(t);
      return new Failure<T>(t);
    }
  }

  public static <T> Try<T> failure(Throwable failure) {
    return new Failure<T>(failure);
  }

  public static <T> Try<T> success(T result) {
    return new Success<T>(result);
  }

  <U> Try<U> map(Function<T, U> f);

  <U> Try<U> flatMap(Function<T, Try<U>> f);
  
  T getOrElse(Function<Throwable, T> fallback);

  Try<T> ifSuccess(Consumer<T> c);

  Try<T> ifFailure(Consumer<Throwable> c);

  Optional<T> success();

  Optional<Throwable> failure();
}

class Success<T> implements Try<T> {
  private final T result;

  protected Success(T result) {
    this.result = result;
  }

  @Override
  public <U> Try<U> map(Function<T, U> f) {
    return new Success<U>(f.apply(result));
  }

  @Override
  public <U> Try<U> flatMap(Function<T, Try<U>> f) {
    return f.apply(result);
  }

  @Override
  public Optional<T> success() {
    return Optional.of(result);
  }

  @Override
  public Optional<Throwable> failure() {
    return Optional.empty();
  }

  @Override
  public Try<T> ifSuccess(Consumer<T> c) {
    try {
      c.accept(result);
    } catch (Throwable t) {
      NonFatalException.verify(t);
      return new Failure<T>(t);
    }
    return this;
  }

  @Override
  public Try<T> ifFailure(Consumer<Throwable> c) {
    return this;
  }

  @Override
  public String toString() {
    return "Try [result=" + result + "]";
  }

  @Override
  public T getOrElse(Function<Throwable, T> fallback) {
    return result;
  }
}

class Failure<T> implements Try<T> {
  private final Throwable failure;

  public Failure(Throwable failure) {
    this.failure = failure;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <U> Try<U> map(Function<T, U> f) {
    return (Try<U>) this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <U> Try<U> flatMap(Function<T, Try<U>> f) {
    return (Try<U>) this;
  }

  @Override
  public Optional<T> success() {
    return Optional.empty();
  }

  @Override
  public Optional<Throwable> failure() {
    return Optional.of(failure);
  }

  @Override
  public Try<T> ifSuccess(Consumer<T> c) {
    return this;
  }

  @Override
  public Try<T> ifFailure(Consumer<Throwable> c) {
    try {
      c.accept(failure);
    } catch (Throwable t) {
      NonFatalException.verify(t);
      return new Failure<T>(t);
    }
    return this;
  }

  @Override
  public String toString() {
    return "Try [failure=" + failure + "]";
  }

  @Override
  public T getOrElse(Function<Throwable, T> fallback) {
    return fallback.apply(failure);
  }
}
