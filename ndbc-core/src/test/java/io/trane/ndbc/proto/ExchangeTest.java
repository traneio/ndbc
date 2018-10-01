package io.trane.ndbc.proto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.future.Future;

public class ExchangeTest {

  private final Duration         timeout     = Duration.ofSeconds(1);
  private final TestUnmarshaller unmarshaler = new TestUnmarshaller();
  private final TestMarshaller   marshaler   = new TestMarshaller();

  @Test
  public void VOID() {
    assertEquals(Future.VOID, Exchange.VOID.run(new TestChannel()));
  }

  @Test
  public void CLOSE() {
    final Channel channel = new TestChannel() {
      @Override
      public Future<Void> close() {
        return Future.VOID;
      }
    };
    assertEquals(Future.VOID, Exchange.CLOSE.run(channel));
  }

  @Test(expected = RuntimeException.class)
  public void failString() throws CheckedFutureException {
    Exchange.fail("error").run(new TestChannel()).get(timeout);
  }

  @Test(expected = RuntimeException.class)
  public void failException() throws CheckedFutureException {
    final RuntimeException ex = new RuntimeException();
    Exchange.fail(ex).run(new TestChannel()).get(timeout);
  }

  @Test
  public void receive() throws CheckedFutureException {
    final TestServerMessage msg = new TestServerMessage();
    final Channel channel = new TestChannel() {
      @SuppressWarnings("unchecked")
      @Override
      public <T extends ServerMessage> Future<T> receive(Unmarshaller<T> u) {
        assertEquals(unmarshaler, u);
        return Future.value((T) msg);
      }
    };
    assertEquals(msg, Exchange.receive(unmarshaler).run(channel).get(timeout));
  }

  @Test
  public void send() throws CheckedFutureException {
    final ClientMessage msg = new ClientMessage() {
    };
    final Channel channel = new TestChannel() {
      @Override
      public <T extends ClientMessage> Future<Void> send(Marshaller<T> ms, T m) {
        assertEquals(marshaler, ms);
        assertEquals(msg, m);
        return Future.VOID;
      }
    };
    Exchange.send(marshaler, msg).run(channel).get(timeout);
  }

  @Test
  public void value() throws CheckedFutureException {
    final int expected = 1;
    final int actual = Exchange.value(expected).run(new TestChannel()).get(timeout);
    assertEquals(expected, actual);
  }

  @Test
  public void map() throws CheckedFutureException {
    assertEquals(new Integer(2), Exchange.value(1).map(i -> i + 1).run(new TestChannel()).get(timeout));
  }

  @Test
  public void flatMap() throws CheckedFutureException {
    assertEquals(new Integer(2),
        Exchange.value(1).flatMap(i -> Exchange.value(i + 1)).run(new TestChannel()).get(timeout));
  }

  @Test
  public void rescue() throws CheckedFutureException {
    final Exception ex = new Exception();
    assertEquals(new Integer(2), Exchange.fail(ex).rescue(e -> {
      assertEquals(ex, e);
      return Exchange.value(2);
    }).run(new TestChannel()).get(timeout));
  }

  @Test
  public void rescueNoError() throws CheckedFutureException {
    assertEquals(new Integer(1), Exchange.value(1).rescue(e -> {
      return Exchange.value(2);
    }).run(new TestChannel()).get(timeout));
  }

  @Test(expected = Exception.class)
  public void onFailureVoid() throws CheckedFutureException {
    final Exception ex = new Exception();
    final AtomicBoolean called = new AtomicBoolean(false);
    final Future<Void> result = Exchange.<Void>fail(ex).onFailure(e -> {
      assertEquals(ex, e);
      called.set(true);
      return Exchange.VOID;
    }).run(new TestChannel());
    assertTrue(called.get());
    result.get(timeout);
  }

  @Test(expected = Exception.class)
  public void onFailureExchange() throws CheckedFutureException {
    final Exception ex = new Exception();
    final ClientMessage msg = new ClientMessage() {
    };
    final AtomicBoolean sent = new AtomicBoolean(false);
    final Future<Void> result = Exchange.<Void>fail(ex).onFailure(e -> {
      assertEquals(ex, e);
      return Exchange.send(marshaler, msg);
    }).run(new TestChannel() {
      @Override
      public <T extends ClientMessage> Future<Void> send(Marshaller<T> marshaller, T m) {
        assertEquals(msg, m);
        sent.set(true);
        return Future.VOID;
      }
    });
    assertTrue(sent.get());
    result.get(timeout);
  }

  @Test
  public void onSuccessVoid() throws CheckedFutureException {
    final AtomicBoolean called = new AtomicBoolean(false);
    final Integer value = 2;
    final Future<Integer> result = Exchange.value(value).onSuccess(v -> {
      assertEquals(value, v);
      called.set(true);
      return Exchange.VOID;
    }).run(new TestChannel());
    assertTrue(called.get());
    assertEquals(value, result.get(timeout));
  }

  @Test
  public void onSuccessExchange() throws CheckedFutureException {
    final Integer value = 2;
    final ClientMessage msg = new ClientMessage() {
    };
    final AtomicBoolean sent = new AtomicBoolean(false);
    final Future<Integer> result = Exchange.value(value).onSuccess(v -> {
      assertEquals(value, v);
      return Exchange.send(marshaler, msg);
    }).run(new TestChannel() {
      @Override
      public <T extends ClientMessage> Future<Void> send(Marshaller<T> marshaller,
          T m) {
        assertEquals(msg, m);
        sent.set(true);
        return Future.VOID;
      }
    });
    assertTrue(sent.get());
    assertEquals(value, result.get(timeout));
  }

  public void then() throws CheckedFutureException {
    assertEquals(new Integer(2),
        Exchange.value(1).then(Exchange.value(2)).run(new TestChannel()).get(timeout));
  }

  @Test
  public void thenReceiveUnmarshaller() throws CheckedFutureException {
    final TestServerMessage msg = new TestServerMessage();
    final Channel channel = new TestChannel() {
      @SuppressWarnings("unchecked")
      @Override
      public <T extends ServerMessage> Future<T> receive(Unmarshaller<T> unmarshaller) {
        return Future.value((T) msg);
      }
    };
    assertEquals(new Integer(1),
        Exchange.value(1).thenReceive(unmarshaler).run(channel).get(timeout));
  }

  @Test(expected = RuntimeException.class)
  public void thenFail() throws CheckedFutureException {
    Exchange.value(1).thenFail("error").run(new TestChannel()).get(timeout);
  }

  @Test
  public void thenSend() throws CheckedFutureException {
    final ClientMessage msg = new ClientMessage() {
    };
    final Channel channel = new TestChannel() {
      @Override
      public <T extends ClientMessage> Future<Void> send(Marshaller<T> marshaller,
          T m) {
        assertEquals(msg, m);
        return Future.VOID;
      }
    };
    Exchange.value(1).thenSend(marshaler, msg).run(channel).get(timeout);
  }

  class TestUnmarshaller implements Unmarshaller<ServerMessage> {
    @Override
    public Optional<ServerMessage> apply(BufferReader bufferReader) {
      return null;
    }
  }

  class TestMarshaller implements Marshaller<ClientMessage> {
    @Override
    public void apply(ClientMessage msg, BufferWriter b) {
    }
  }

  class TestServerMessage implements ServerMessage {
  }

  class TestChannel implements Channel {

    @Override
    public Future<Void> close() {
      return notExpected();
    }

    @Override
    public <T extends ClientMessage> Future<Void> send(Marshaller<T> marshaller,
        T msg) {
      return notExpected();
    }

    @Override
    public <T extends ServerMessage> Future<T> receive(Unmarshaller<T> unmarshaller) {
      return notExpected();
    }

    private <T> T notExpected() {
      throw new IllegalStateException("Unpexted call");
    }
  }
}
