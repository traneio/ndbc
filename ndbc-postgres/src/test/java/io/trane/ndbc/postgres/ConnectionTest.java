package io.trane.ndbc.postgres;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.future.Future;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.postgres.proto.Message.BackendKeyData;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.ClientMessage;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.value.IntegerValue;
import io.trane.ndbc.value.Value;

public class ConnectionTest {

  Duration timeout = Duration.ofSeconds(1);

  @Test
  public void query() throws CheckedFutureException {
    List<Row> result = new ArrayList<>();
    String query = "query";
    Supplier<Connection> sup = new ConnectionSupplier() {
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
  public void execute() throws CheckedFutureException {
    Integer result = 33;
    String command = "command";
    Supplier<Connection> sup = new ConnectionSupplier() {
      @Override
      Function<String, Exchange<Integer>> simpleExecuteExchange() {
        return q -> {
          assertEquals(command, q);
          return Exchange.value(result);
        };
      }
    };
    assertEquals(result, sup.get().execute(command).get(timeout));
  }

  @Test
  public void queryPreparedStatement() throws CheckedFutureException {
    List<Row> result = new ArrayList<>();
    String query = "query";
    Integer bind = 223;
    PreparedStatement ps = PreparedStatement.apply(query).bindInteger(bind);
    Supplier<Connection> sup = new ConnectionSupplier() {
      @Override
      BiFunction<String, List<Value<?>>, Exchange<List<Row>>> extendedQueryExchange() {
        return (q, b) -> {
          assertEquals(query, q);
          assertEquals(Arrays.asList(new IntegerValue(bind)), b);
          return Exchange.value(result);
        };
      }
    };
    assertEquals(result, sup.get().query(ps).get(timeout));
  }

  @Test
  public void executePreparedStatement() throws CheckedFutureException {
    Integer result = 413;
    String command = "command";
    Integer bind = 223;
    PreparedStatement ps = PreparedStatement.apply(command).bindInteger(bind);
    Supplier<Connection> sup = new ConnectionSupplier() {
      @Override
      BiFunction<String, List<Value<?>>, Exchange<Integer>> extendedExecuteExchange() {
        return (c, b) -> {
          assertEquals(command, c);
          assertEquals(Arrays.asList(new IntegerValue(bind)), b);
          return Exchange.value(result);
        };
      }
    };
    assertEquals(result, sup.get().execute(ps).get(timeout));
  }

  @Test
  public void isValidTrue() throws CheckedFutureException {
    List<Row> result = new ArrayList<>();
    String query = "SELECT 1";
    Supplier<Connection> sup = new ConnectionSupplier() {
      @Override
      BiFunction<String, List<Value<?>>, Exchange<List<Row>>> extendedQueryExchange() {
        return (q, b) -> {
          assertEquals(query, q);
          return Exchange.value(result);
        };
      }
    };
    assertEquals(true, sup.get().isValid().get(timeout));
  }

  @Test
  public void isValidFalse() throws CheckedFutureException {
    String query = "SELECT 1";
    Supplier<Connection> sup = new ConnectionSupplier() {
      @Override
      BiFunction<String, List<Value<?>>, Exchange<List<Row>>> extendedQueryExchange() {
        return (q, b) -> {
          assertEquals(query, q);
          return Exchange.fail("error");
        };
      }
    };
    assertEquals(false, sup.get().isValid().get(timeout));
  }

  @Test
  public void close() throws CheckedFutureException {
    AtomicBoolean called = new AtomicBoolean(false);
    Supplier<Connection> sup = new ConnectionSupplier() {
      @Override
      Channel channel() {
        return new TestChannel() {
          @Override
          public Future<Void> close() {
            called.set(true);
            return Future.VOID;
          }
        };
      }
    };
    sup.get().close().get(timeout);
    assertTrue(called.get());
  }

  class TestChannel implements Channel {

    @Override
    public Future<Void> send(ClientMessage msg) {
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

    Optional<BackendKeyData> backendKeyData() {
      return Optional.empty();
    }

    Function<String, Exchange<List<Row>>> simpleQueryExchange() {
      return v -> notExpected();
    }

    Function<String, Exchange<Integer>> simpleExecuteExchange() {
      return v -> notExpected();
    }

    BiFunction<String, List<Value<?>>, Exchange<List<Row>>> extendedQueryExchange() {
      return (a, b) -> notExpected();
    }

    BiFunction<String, List<Value<?>>, Exchange<Integer>> extendedExecuteExchange() {
      return (a, b) -> notExpected();
    }

    @Override
    public Connection get() {
      return new Connection(channel(), channelSupplier(), backendKeyData(), simpleQueryExchange(),
          simpleExecuteExchange(), extendedQueryExchange(), extendedExecuteExchange());
    }
  }

  private <T> T notExpected() {
    throw new IllegalStateException("Unexpected");
  }

}
