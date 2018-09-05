package io.trane.ndbc.proto;

import java.util.function.Function;

import io.trane.future.Future;
import io.trane.ndbc.NdbcException;

@FunctionalInterface
public interface Exchange<T> {

  static Exchange<Void> VOID  = channel -> Future.VOID;
  static Exchange<Void> CLOSE = Channel::close;

  static <R> Exchange<R> fail(final String error) {
    return fail(new NdbcException(error));
  }

  static <R> Exchange<R> fail(final Throwable ex) {
    final Future<Void> result = Future.exception(ex);
    return channel -> result.unsafeCast();
  }

  static <T extends ClientMessage> Exchange<Void> send(final Marshaller<T> marshaller, final T msg) {
    return channel -> channel.send(marshaller, msg);
  }

  static <T extends ServerMessage> Exchange<T> receive(final Unmarshaller<T> unmarshaller) {
    return channel -> channel.receive(unmarshaller);
  }

  static <T> Exchange<T> value(final T v) {
    return channel -> Future.value(v);
  }

  Future<T> run(Channel channel);

  default public <R> Exchange<R> map(final Function<T, R> f) {
    return channel -> run(channel).map(f::apply);
  }

  default public <R> Exchange<R> flatMap(final Function<T, Exchange<R>> f) {
    return channel -> run(channel).flatMap(v -> f.apply(v).run(channel));
  }

  default public Exchange<T> rescue(final Function<Throwable, Exchange<T>> f) {
    return channel -> run(channel).rescue(t -> f.apply(t).run(channel));
  }

  default public Exchange<T> onFailure(final Function<Throwable, Exchange<?>> e) {
    return channel -> run(channel).rescue(ex -> e.apply(ex).run(channel).flatMap(v -> Future.exception(ex)));
  }

  default public Exchange<T> onSuccess(final Function<T, Exchange<?>> f) {
    return channel -> run(channel).flatMap(v -> f.apply(v).run(channel).map(ign -> v));
  }

  default public <R> Exchange<R> then(final Exchange<R> ex) {
    return channel -> run(channel).flatMap(v -> ex.run(channel));
  }

  default public <R> Exchange<R> thenWaitFor(final Exchange<R> result) {
    return rescue(ex -> result.flatMap(v -> Exchange.fail(ex))).flatMap(r -> result);
  }

  default public <U extends ServerMessage> Exchange<T> thenReceive(final Unmarshaller<U> unmarshaller) {
    return flatMap(v -> Exchange.receive(unmarshaller).map(i -> v));
  }

  default public Exchange<T> thenWaitFor(final Unmarshaller<? extends ServerMessage> unmarshaller) {
    return rescue(ex -> Exchange.receive(unmarshaller).flatMap(v -> Exchange.fail(ex)))
        .flatMap(r -> Exchange.receive(unmarshaller).map(v -> r));
  }

  default public <R> Exchange<R> thenFail(final String error) {
    return then(Exchange.fail(error));
  }

  default public <U extends ClientMessage> Exchange<Void> thenSend(final Marshaller<U> marshaller, final U msg) {
    return then(Exchange.send(marshaller, msg));
  }
}
