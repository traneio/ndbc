package io.trane.ndbc.sqlserver;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.future.Future;
import io.trane.ndbc.Row;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.ClientMessage;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.Marshaller;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.proto.Unmarshaller;

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

  class TestChannel implements Channel {

    @Override
    public Future<Void> close() {
      return notExpected();
    }

    @Override
    public <T extends ClientMessage> Future<Void> send(final Marshaller<T> marshaller, final T msg) {
      return notExpected();
    }

    @Override
    public <T extends ServerMessage> Future<T> receive(final Unmarshaller<T> unmarshaller) {
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

    @Override
    public Connection get() {
      return new Connection(channel(), null, Optional.empty(), null, channelSupplier(), simpleQueryExchange());
    }
  }

  private <T> T notExpected() {
    throw new IllegalStateException("Unexpected");
  }
}
