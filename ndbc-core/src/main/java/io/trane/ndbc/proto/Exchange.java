package io.trane.ndbc.proto;

import io.trane.future.Future;
import io.trane.ndbc.util.PartialFunction;

@FunctionalInterface
public interface Exchange<T> {

  static class ExchangeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExchangeException(final String message) {
      super(message);
    }
  }

  static Exchange<Void> close() {
    return Channel::close;
  }

  static Exchange<Void> done() {
    return channel -> Future.VOID;
  }

  static <R> Exchange<R> fail(final String error) {
    final Future<Void> result = Future.exception(new ExchangeException(error));
    return channel -> result.unsafeCast();
  }

  static <R> Exchange<R> receive(final PartialFunction<ServerMessage, Exchange<R>> f) {
    return channel -> channel.receive().flatMap(
        msg -> f.applyOrElse(msg, () -> Exchange.fail("Unexpected database message: " + msg)).run(channel));
  }

  static Exchange<Void> send(final ClientMessage msg) {
    return channel -> channel.send(msg);
  }

  static <T> Exchange<T> value(final T v) {
    return channel -> Future.value(v);
  }

  Future<T> run(Channel channel);

  default public <R> Exchange<R> then(final Exchange<R> ex) {
    return channel -> run(channel).flatMap(v -> ex.run(channel));
  }

  default public <R> Exchange<R> thenReceive(final PartialFunction<ServerMessage, Exchange<R>> f) {
    return then(Exchange.receive(f));
  }

  default public <R> Exchange<R> thenFail(final String error) {
    return then(Exchange.fail(error));
  }

  default public Exchange<Void> thenSend(final ClientMessage msg) {
    return then(Exchange.send(msg));
  }
}
