package io.trane.ndbc.mysql;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.future.Future;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.mysql.proto.Message.BackendKeyData;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.ClientMessage;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.value.IntegerValue;
import io.trane.ndbc.value.Value;

public class ConnectionTest {
	
	private Duration timeout = Duration.ofSeconds(1);

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

		Optional<BackendKeyData> backendKeyData() {
			return Optional.empty();
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
			return new Connection(channel(), channelSupplier(), simpleQueryExchange(), extendedQueryExchange());
		}
	}

	private <T> T notExpected() {
		throw new IllegalStateException("Unexpected");
	}
}