package io.trane.ndbc.proto;

import java.util.function.Function;
import java.util.logging.Logger;

import io.trane.future.Future;
import io.trane.ndbc.util.PartialFunction;

@FunctionalInterface
public interface Exchange<T> {

  static Logger         log   = Logger.getLogger(Exchange.class.getName());

  static Exchange<Void> VOID  = channel -> Future.VOID;
  static Exchange<Void> CLOSE = Channel::close;

  static <R> Exchange<R> fail(final String error) {
    return fail(new RuntimeException(error));
  }

  static <R> Exchange<R> fail(final Throwable ex) {
    final Future<Void> result = Future.exception(ex);
    return channel -> result.unsafeCast();
  }

  static <R> Exchange<R> receive(final PartialFunction<ServerMessage, Exchange<R>> f) {
    return channel -> channel.receive().flatMap(msg -> {
      if (msg.isNotice()) {
        log.info(msg.toString());
        return receive(f).run(channel);
      } else if (msg.isError())
        return Future.exception(new RuntimeException(msg.toString()));
      else
        return f.applyOrElse(msg, () -> Exchange.fail("Unexpected server message: " + msg))
            .run(channel);
    });
  }

  static Exchange<Void> send(final ClientMessage msg) {
    return channel -> channel.send(msg);
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
    return channel -> run(channel)
        .rescue(ex -> e.apply(ex).run(channel).flatMap(v -> Future.exception(ex)));
  }

  default public Exchange<T> onSuccess(final Function<T, Exchange<?>> f) {
    return channel -> run(channel).flatMap(v -> f.apply(v).run(channel).map(ign -> v));
  }

  default public <R> Exchange<R> then(final Exchange<R> ex) {
    return channel -> run(channel).flatMap(v -> ex.run(channel));
  }

  default public Exchange<T> thenReceive(final Class<? extends ServerMessage> cls) {
    return flatMap(v -> Exchange.receive(PartialFunction.when(cls, msg -> Exchange.value(v))));
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
