package io.trane.ndbc.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Try<T> {

	public static <T> Try<T> apply(final Supplier<T> supplier) {
		try {
			return new Success<>(supplier.get());
		} catch (final Exception t) {
			NonFatalException.verify(t);
			return new Failure<>(t);
		}
	}

	public static <T> Try<T> failure(final Throwable failure) {
		return new Failure<>(failure);
	}

	public static <T> Try<T> success(final T result) {
		return new Success<>(result);
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

	protected Success(final T result) {
		this.result = result;
	}

	@Override
	public <U> Try<U> map(final Function<T, U> f) {
		return new Success<>(f.apply(result));
	}

	@Override
	public <U> Try<U> flatMap(final Function<T, Try<U>> f) {
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
	public Try<T> ifSuccess(final Consumer<T> c) {
		try {
			c.accept(result);
		} catch (final Throwable t) {
			NonFatalException.verify(t);
			return new Failure<>(t);
		}
		return this;
	}

	@Override
	public Try<T> ifFailure(final Consumer<Throwable> c) {
		return this;
	}

	@Override
	public String toString() {
		return "Try [result=" + result + "]";
	}

	@Override
	public T getOrElse(final Function<Throwable, T> fallback) {
		return result;
	}
}

class Failure<T> implements Try<T> {
	private final Throwable failure;

	public Failure(final Throwable failure) {
		this.failure = failure;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U> Try<U> map(final Function<T, U> f) {
		return (Try<U>) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U> Try<U> flatMap(final Function<T, Try<U>> f) {
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
	public Try<T> ifSuccess(final Consumer<T> c) {
		return this;
	}

	@Override
	public Try<T> ifFailure(final Consumer<Throwable> c) {
		try {
			c.accept(failure);
		} catch (final Throwable t) {
			NonFatalException.verify(t);
			return new Failure<>(t);
		}
		return this;
	}

	@Override
	public String toString() {
		return "Try [failure=" + failure + "]";
	}

	@Override
	public T getOrElse(final Function<Throwable, T> fallback) {
		return fallback.apply(failure);
	}
}
