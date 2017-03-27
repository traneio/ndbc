//package io.trane.ndbc.postgres.proto;
//
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.logging.Logger;
//
//import io.trane.future.Future;
//import io.trane.ndbc.postgres.proto.Message.StartupMessage;
//import io.trane.ndbc.util.PartialFunction;
//
//import static io.trane.ndbc.postgres.proto.Message.*;
//
//public class Startup {
//
//  private static final Logger log = Logger.getLogger("Startup");
//
//  private final Channel channel;
//
//  public Startup(Channel channel) {
//    super();
//    this.channel = channel;
//  }
//
//  public Exchange<Void> startup(Charset charset, String user, Optional<String> database, Optional<String> password) {
//
//    Exchange.apply().send(startupMessage(charset, user, database)).receive(PartialFunction
//        .<BackendMessage, Exchange<Void>>when(AuthenticationRequest.AuthenticationOk.class, msg -> Exchange.done()));
//
//    // return exchange.send(startupMessage(charset, user, database))
//    // .receive(PartialFunction.apply().when(AuthenticationRequest.AuthenticationOk.class,
//    // apply));
//
//    return null;
//  }
//
//  // public Future<Void> startup(Charset charset, String user, Optional<String>
//  // database, Optional<String> password) {
//  // return sendStartupMessage(charset, user, database).flatMap(v1 -> {
//  //
//  // channel.receive().flatMap(msg -> {
//  // if (msg instanceof AuthenticationRequest.AuthenticationOk) {
//  // if (password.isPresent())
//  // return fail("Password is defined but the database doesn't require
//  // authentication.");
//  // else
//  // return Future.VOID;
//  // } else if (msg instanceof
//  // AuthenticationRequest.AuthenticationCleartextPassword) {
//  // password.map(p -> {
//  // channel.send(new PasswordMessage(p)).flatMap(f)
//  // })
//  // .orElse(fail("Database requires password but it is not configured"));
//  // }
//  // });
//  // return Future.VOID;
//  // });
//  // }
//
//  private StartupMessage startupMessage(Charset charset, String user, Optional<String> database) {
//    List<StartupMessage.Parameter> params = new ArrayList<>();
//    database.ifPresent(db -> params.add(new StartupMessage.Parameter("database", db)));
//    params.add(new StartupMessage.Parameter("client_encoding", charset.name()));
//    params.add(new StartupMessage.Parameter("DateStyle", "ISO"));
//    params.add(new StartupMessage.Parameter("extra_float_digits", "2"));
//    return new StartupMessage(user, params.toArray(new StartupMessage.Parameter[0]));
//  }
//}
