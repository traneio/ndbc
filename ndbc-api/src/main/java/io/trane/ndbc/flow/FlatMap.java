// package io.trane.ndbc.flow;
//
// import java.util.Deque;
// import java.util.concurrent.ConcurrentLinkedDeque;
// import java.util.concurrent.atomic.AtomicLong;
// import java.util.function.Function;
//
// import org.reactivestreams.Subscriber;
// import org.reactivestreams.Subscription;
//
// final class FlatMap<T, U> implements Flow<U> {
//
// private final Flow<T> flow;
// private final Function<T, Flow<U>> f;
//
// public FlatMap(Flow<T> flow, Function<T, Flow<U>> f) {
// this.flow = flow;
// this.f = f;
// }
//
// @Override
// public void subscribe(Subscriber<? super U> sb) {
// flow.subscribe(new Subscriber<T>() {
//
// Deque<Subscription> subscriptions = new ConcurrentLinkedDeque<>();
// AtomicLong lastRequest = new AtomicLong(0L);
// AtomicLong pending = new AtomicLong(0L);
//
// Subscription coord = new Subscription() {
// @Override
// public void request(final long n) {
// Scheduler.submit(() -> {
// if (pending.get() >= 0) {
// if (subscriptions.isEmpty()) {
// pending.set(-1);
// sb.onComplete();
// } else {
// pending.set(n);
// subscriptions.peekFirst().request(n);
// }
// }
// });
// }
//
// @Override
// public void cancel() {
// subscriptions.forEach(s -> s.cancel());
// }
// };
//
// @Override
// public void onSubscribe(Subscription s) {
// subscriptions.add(s);
// sb.onSubscribe(new Subscription() {
//
// @Override
// public void request(long n) {
// lastRequest.set(n);
// coord.request(n);
// }
//
// @Override
// public void cancel() {
// coord.cancel();
// }
// });
// }
//
// @Override
// public void onNext(T t) {
// f.apply(t).subscribe(new Subscriber<U>() {
//
// private Subscription s = null;
//
// @Override
// public void onSubscribe(Subscription s) {
// this.s = s;
// Subscription prevLast = subscriptions.removeLast();
// subscriptions.addLast(s);
// subscriptions.addLast(prevLast);
// }
//
// @Override
// public void onNext(U t) {
// sb.onNext(t);
// pending.decrementAndGet();
// }
//
// @Override
// public void onError(Throwable t) {
// subscriptions.forEach(s -> s.cancel());
// sb.onError(t);
// }
//
// @Override
// public void onComplete() {
// subscriptions.removeFirstOccurrence(s);
// long p = pending.get();
// if (p > 0)
// coord.request(p);
// }
// });
// long p = pending.decrementAndGet();
// if (p == 0L)
// coord.request(lastRequest.get());
// }
//
// @Override
// public void onError(Throwable t) {
// subscriptions.forEach(s -> s.cancel());
// sb.onError(t);
// }
//
// @Override
// public void onComplete() {
// subscriptions.removeLast();
// coord.request(lastRequest.get());
// }
// });
// }
//
// }
