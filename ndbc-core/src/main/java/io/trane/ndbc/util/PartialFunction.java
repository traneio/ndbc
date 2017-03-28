package io.trane.ndbc.util;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface PartialFunction<T, U> {

  public static <T, U> PartialFunction<T, U> apply() {
    return (value, fallback) -> fallback.get();
  }

  public static <T, U, X extends T> PartialFunction<T, U> when(final Class<X> cls, final Function<X, U> apply) {
    return new PartialFunction<T, U>() {
      @SuppressWarnings("unchecked")
      @Override
      public U applyOrElse(final T value, final Supplier<U> fallback) {
        if (cls.isInstance(value))
          return apply.apply((X) value);
        else
          return fallback.get();
      }
    };
  }

  public U applyOrElse(T value, Supplier<U> fallback);

  default public PartialFunction<T, U> orElse(final Predicate<T> isDefinedAt, final Function<T, U> apply) {
    return orElse((value, fallback) -> {
      if (isDefinedAt.test(value))
        return apply.apply(value);
      else
        return fallback.get();
    });
  }

  default public <X extends T> PartialFunction<T, U> orElse(final Class<X> cls, final Function<X, U> apply) {
    return orElse(new PartialFunction<T, U>() {
      @SuppressWarnings("unchecked")
      @Override
      public U applyOrElse(final T value, final Supplier<U> fallback) {
        if (cls.isInstance(value))
          return apply.apply((X) value);
        else
          return fallback.get();
      }
    });
  }

  default public PartialFunction<T, U> orElse(final PartialFunction<? super T, U> pf) {
    return (value, fallback) -> PartialFunction.this.applyOrElse(value, () -> pf.applyOrElse(value, fallback));
  }

  default public Function<T, Optional<U>> lift() {
    return value -> Optional.ofNullable(applyOrElse(value, null));
  }
}
