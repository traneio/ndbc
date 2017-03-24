package io.trane.ndbc.util;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface PartialFunction<T, U> {

  public static <T, U> PartialFunction<T, U> apply() {
    return new PartialFunction<T, U>() {
      @Override
      public U applyOrElse(T value, Supplier<U> fallback) {
        return fallback.get();
      }
    };
  }

  public U applyOrElse(T value, Supplier<U> fallback);

  default public PartialFunction<T, U> when(Predicate<T> isDefinedAt, Function<T, U> apply) {
    return orElse(new PartialFunction<T, U>() {
      @Override
      public U applyOrElse(T value, Supplier<U> fallback) {
        if (isDefinedAt.test(value))
          return apply.apply(value);
        else
          return fallback.get();
      }
    });
  }

  default public PartialFunction<T, U> orElse(PartialFunction<T, U> pf) {
    return new PartialFunction<T, U>() {
      @Override
      public U applyOrElse(T value, Supplier<U> fallback) {
        return PartialFunction.this.applyOrElse(value, () -> pf.applyOrElse(value, fallback));
      }
    };
  }

  default public Function<T, Optional<U>> lift() {
    return value -> Optional.ofNullable(applyOrElse(value, null));
  }
}
