package io.trane.ndbc.mysql;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.future.Future;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.proto.*;
import io.trane.ndbc.value.IntegerValue;
import io.trane.ndbc.value.Value;

public class ConnectionTest {

  private final Duration timeout = Duration.ofSeconds(1);

  @Test
  public void query() throws CheckedFutureException {
    final List<Row> result = new ArrayList<>();
    final String query = "query";
    final Supplier<Connection> sup = new ConnectionSupplier() {
      @Override
      Function<String, Exchange<List<Row>>> simpleQueryExchange() {
        return q -> {
          assertEquals(query, q);
          return Exchange.value(result);
        };
      }
    };
    assertEquals(result, sup.get().query(query).get(timeout));
  }

  @Test
  public void queryPreparedStatement() throws CheckedFutureException {
    final List<Row> result = new ArrayList<>();
    final String query = "query";
    final Integer set = 123;
    final PreparedStatement ps = PreparedStatement.apply(query).setInteger(set);
    final Supplier<Connection> sup = new ConnectionSupplier() {
      @Override
      BiFunction<String, List<Value<?>>, Exchange<List<Row>>> extendedQueryExchange() {
        return (q, b) -> {
          assertEquals(query, q);
          assertEquals(Arrays.asList(new IntegerValue(set)), b);
          return Exchange.value(result);
        };
      }
    };
    assertEquals(result, sup.get().query(ps).get(timeout));
  }

  @Test
  public void execute() throws CheckedFutureException {
    final Long result = 33L;
    final String command = "command";
    final Supplier<Connection> sup = new ConnectionSupplier() {
      @Override
      Function<String, Exchange<Long>> simpleExecuteExchange() {
        return q -> {
          assertEquals(command, q);
          return Exchange.value(result);
        };
      }
    };
    assertEquals(result, sup.get().execute(command).get(timeout));
  }

  @Test
  public void executePreparedStatement() throws CheckedFutureException {
    final Long result = 413L;
    final String command = "command";
    final Integer set = 223;
    final PreparedStatement ps = PreparedStatement.apply(command).setInteger(set);
    final Supplier<Connection> sup = new ConnectionSupplier() {
      @Override
      BiFunction<String, List<Value<?>>, Exchange<Long>> extendedExecuteExchange() {
        return (c, b) -> {
          assertEquals(command, c);
          assertEquals(Arrays.asList(new IntegerValue(set)), b);
          return Exchange.value(result);
        };
      }
    };
    assertEquals(result, sup.get().execute(ps).get(timeout));
  }

  class TestChannel implements Channel {

    @Override
    public Future<Void> send(final ClientMessage msg) {
      return notExpected();
    }

    @Override
    public Future<ServerMessage> receive() {
      return notExpected();
    }

    @Override
    public Future<Void> close() {
      return notExpected();
    }
  };

  class ConnectionSupplier implements Supplier<Connection> {
    Channel channel() {
      return new TestChannel();
    }

    Supplier<? extends Future<? extends Channel>> channelSupplier() {
      return () -> notExpected();
    }

    Function<String, Exchange<List<Row>>> simpleQueryExchange() {
      return v -> notExpected();
    }

    Function<String, Exchange<Long>> simpleExecuteExchange() {
      return v -> notExpected();
    }

    BiFunction<String, List<Value<?>>, Exchange<List<Row>>> extendedQueryExchange() {
      return (a, b) -> notExpected();
    }

    BiFunction<String, List<Value<?>>, Exchange<Long>> extendedExecuteExchange() {
      return (a, b) -> notExpected();
    }

    @Override
    public Connection get() {
      return new Connection(channel(), channelSupplier(), simpleQueryExchange(), simpleExecuteExchange(),
          extendedQueryExchange(), extendedExecuteExchange());
    }
  }

  private <T> T notExpected() {
    throw new IllegalStateException("Unexpected");
  }
}