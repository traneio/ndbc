package io.trane.ndbc.postgres.proto;

import java.util.function.Function;

import io.trane.future.Future;
import io.trane.ndbc.postgres.proto.Message.BackendMessage;
import io.trane.ndbc.postgres.proto.Message.FrontendMessage;
import io.trane.ndbc.util.PartialFunction;

@FunctionalInterface
interface Exchange<T> {

  static Exchange<Void> apply() {
    return new Exchange<Void>() {
      @Override
      public Future<Void> apply(Channel channel) {
        return Future.VOID;
      }
    };
  }

  static Exchange<Void> done() {
    return apply();
  }

  class ExchangeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExchangeException(String message) {
      super(message);
    }
  }

  static <R> Exchange<R> fail(String error) {
    final Future<Void> result = Future.exception(new ExchangeException(error));
    return new Exchange<R>() {
      @Override
      public Future<R> apply(Channel channel) {
        return result.unsafeCast();
      }
    };
  }

  Future<T> apply(Channel channel);

  default public Exchange<Void> send(FrontendMessage msg) {
    return new Exchange<Void>() {
      @Override
      public Future<Void> apply(Channel channel) {
        return channel.send(msg);
      }
    };
  }

  default public <R> Exchange<R> receive(PartialFunction<BackendMessage, Exchange<R>> f) {
    return new Exchange<R>() {
      @Override
      public Future<R> apply(Channel channel) {
        return channel.receive().flatMap(
            msg -> f.applyOrElse(msg, () -> Exchange.fail("Unexpected database message: " + msg)).apply(channel));
      }
    };
  }
}
