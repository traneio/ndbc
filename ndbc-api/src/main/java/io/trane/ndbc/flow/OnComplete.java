package io.trane.ndbc.flow;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

final class OnComplete<T> implements Flow<T> {

	private final Flow<T> flow;
	private final Runnable f;

	public OnComplete(Flow<T> flow, Runnable f) {
		this.flow = flow;
		this.f = f;
	}

	@Override
	public void subscribe(Subscriber<? super T> s) {
		flow.subscribe(new Subscriber<T>() {

			@Override
			public void onSubscribe(Subscription t) {
				s.onSubscribe(t);
			}

			@Override
			public void onComplete() {
				f.run();
				s.onComplete();
			}

			@Override
			public void onError(Throwable t) {
				s.onError(t);
			}

			@Override
			public void onNext(T t) {
				s.onNext(t);
			}
		});

	}

}
