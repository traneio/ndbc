package io.trane.ndbc.flow;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.reactivestreams.Publisher;

import io.trane.future.Future;

/**
 * A composable TCK-compliant reactive streams `Publisher` implementation.
 * 
 * @param <T>
 *            the stream element type
 */
@FunctionalInterface
public interface Flow<T> extends Publisher<T> {

	public static <U> Flow<U> from(Publisher<U> publisher) {
		return publisher::subscribe;
	}

	public static <U> Flow<U> from(List<U> list) {
		if (list.isEmpty())
			return empty();
		else
			return new FromList<>(list);
	}

	@SafeVarargs
	public static <U> Flow<U> from(U... list) {
		return from(Arrays.asList(list));
	}

	public static <U> Flow<U> batched(Function<Long, Flow<U>> fetch) {
		return new Batched<>(fetch);
	}

	public static <U> Flow<U> empty() {
		return new Empty<>();
	}

	public static <U> Flow<U> from(Future<Flow<U>> fut) {
		return new FromFuture<>(fut);
	}

	default Collect<T> collect(long requestSize) {
		return new Collect<>(this, requestSize);
	}

	default Flow<T> filter(Predicate<T> p) {
		return new Filter<>(this, p);
	}

	default Flow<T> onComplete(Runnable f) {
		return new OnComplete<>(this, f);
	}

	default <U> Flow<U> map(Function<T, U> f) {
		return new Map<>(this, f);
	}
}
