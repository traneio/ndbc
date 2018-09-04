// package io.trane.ndbc.mysql;
//
// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertTrue;
//
// import java.time.Duration;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.util.concurrent.atomic.AtomicBoolean;
// import java.util.function.BiFunction;
// import java.util.function.Function;
// import java.util.function.Supplier;
//
// import org.junit.Ignore;
// import org.junit.Test;
//
// import io.trane.future.CheckedFutureException;
// import io.trane.future.Future;
// import io.trane.ndbc.PreparedStatement;
// import io.trane.ndbc.Row;
// import io.trane.ndbc.proto.Channel;
// import io.trane.ndbc.proto.ClientMessage;
// import io.trane.ndbc.proto.Exchange;
// import io.trane.ndbc.proto.Marshaller;
// import io.trane.ndbc.proto.ServerMessage;
// import io.trane.ndbc.proto.Unmarshaller;
// import io.trane.ndbc.value.IntegerValue;
// import io.trane.ndbc.value.Value;
//
// public class ConnectionTest {
//
// private final Duration timeout = Duration.ofSeconds(1);
//
// @Test
// public void query() throws CheckedFutureException {
// final List<Row> result = new ArrayList<>();
// final String query = "query";
// final Supplier<Connection> sup = new ConnectionSupplier() {
// @Override
// Function<String, Exchange<List<Row>>> simpleQueryExchange() {
// return q -> {
// assertEquals(query, q);
// return Exchange.value(result);
// };
// }
// };
// assertEquals(result, sup.get().query(query).get(timeout));
// }
//
// @Test
// public void queryPreparedStatement() throws CheckedFutureException {
// final List<Row> result = new ArrayList<>();
// final String query = "query";
// final Integer set = 123;
// final PreparedStatement ps = PreparedStatement.apply(query).setInteger(set);
// final Supplier<Connection> sup = new ConnectionSupplier() {
// @Override
// BiFunction<String, List<Value<?>>, Exchange<List<Row>>>
// extendedQueryExchange() {
// return (q, b) -> {
// assertEquals(query, q);
// assertEquals(Arrays.asList(new IntegerValue(set)), b);
// return Exchange.value(result);
// };
// }
// };
// assertEquals(result, sup.get().query(ps).get(timeout));
// }
//
// @Test
// @Ignore
// public void execute() throws CheckedFutureException {
// final Long result = 33L;
// final String command = "command";
// final Supplier<Connection> sup = new ConnectionSupplier() {
// @Override
// Function<String, Exchange<Long>> simpleExecuteExchange() {
// return q -> {
// assertEquals(command, q);
// return Exchange.value(result);
// };
// }
// };
// assertEquals(result, sup.get().execute(command).get(timeout));
// }
//
// @Test
// public void executePreparedStatement() throws CheckedFutureException {
// final Long result = 413L;
// final String command = "command";
// final Integer set = 223;
// final PreparedStatement ps =
// PreparedStatement.apply(command).setInteger(set);
// final Supplier<Connection> sup = new ConnectionSupplier() {
// @Override
// BiFunction<String, List<Value<?>>, Exchange<Long>> extendedExecuteExchange()
// {
// return (c, b) -> {
// assertEquals(command, c);
// assertEquals(Arrays.asList(new IntegerValue(set)), b);
// return Exchange.value(result);
// };
// }
// };
// assertEquals(result, sup.get().execute(ps).get(timeout));
// }
//
// @Test
// public void isValidTrue() throws CheckedFutureException {
// final List<Row> result = new ArrayList<>();
// final String query = "SELECT 1";
// final Supplier<Connection> sup = new ConnectionSupplier() {
// @Override
// BiFunction<String, List<Value<?>>, Exchange<List<Row>>>
// extendedQueryExchange() {
// return (q, b) -> {
// assertEquals(query, q);
// return Exchange.value(result);
// };
// }
// };
// assertEquals(true, sup.get().isValid().get(timeout));
// }
//
// @Test
// public void isValidFalse() throws CheckedFutureException {
// final String query = "SELECT 1";
// final Supplier<Connection> sup = new ConnectionSupplier() {
// @Override
// BiFunction<String, List<Value<?>>, Exchange<List<Row>>>
// extendedQueryExchange() {
// return (q, b) -> {
// assertEquals(query, q);
// return Exchange.fail("error");
// };
// }
// };
// assertEquals(false, sup.get().isValid().get(timeout));
// }
//
// @Test
// public void close() throws CheckedFutureException {
// final AtomicBoolean called = new AtomicBoolean(false);
// final Supplier<Connection> sup = new ConnectionSupplier() {
// @Override
// Channel channel() {
// return new TestChannel() {
// @Override
// public Future<Void> close() {
// called.set(true);
// return Future.VOID;
// }
// };
// }
// };
// sup.get().close().get(timeout);
// assertTrue(called.get());
// }
//
// class TestChannel implements Channel {
//
// @Override
// public Future<Void> close() {
// return notExpected();
// }
//
// @Override
// public <T extends ClientMessage> Future<Void> send(Marshaller<T> marshaller,
// T msg) {
// return notExpected();
// }
//
// @Override
// public <T extends ServerMessage> Future<T> receive(Unmarshaller<T>
// unmarshaller) {
// return notExpected();
// }
// };
//
// class ConnectionSupplier implements Supplier<Connection> {
// Channel channel() {
// return new TestChannel();
// }
//
// Supplier<? extends Future<? extends Channel>> channelSupplier() {
// return () -> notExpected();
// }
//
// Function<String, Exchange<List<Row>>> simpleQueryExchange() {
// return v -> notExpected();
// }
//
// Function<String, Exchange<Long>> simpleExecuteExchange() {
// return v -> Exchange.fail("");
// }
//
// BiFunction<String, List<Value<?>>, Exchange<List<Row>>>
// extendedQueryExchange() {
// return (a, b) -> notExpected();
// }
//
// BiFunction<String, List<Value<?>>, Exchange<Long>> extendedExecuteExchange()
// {
// return (a, b) -> notExpected();
// }
//
// @Override
// public Connection get() {
// return new Connection(channel(), "0", channelSupplier(),
// simpleQueryExchange(), simpleExecuteExchange(),
// extendedQueryExchange(), extendedExecuteExchange());
// }
// }
//
// private <T> T notExpected() {
// throw new IllegalStateException("Unexpected");
// }
// }