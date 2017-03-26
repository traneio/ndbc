//package io.trane.ndbc.postgres.proto;
//
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import io.trane.future.Future;
//
//public class Protocol {
//
//  private final Channel channel;
//
//  public Protocol(Channel channel) {
//    super();
//    this.channel = channel;
//  }
//
//  public Future<Void> startup(Charset charset, String user, Optional<String> database, Optional<String> password) {
//    return channel.send(new Message.StartupMessage() {
//      @Override
//      public String user() {
//        return user;
//      }
//
//      private Parameter parameter(String name, String value) {
//        return new Message.StartupMessage.Parameter() {
//          @Override
//          public String value() {
//            return value;
//          }
//
//          @Override
//          public String name() {
//            return name;
//          }
//        };
//      }
//
//      @Override
//      public Parameter[] parameters() {
//        List<Parameter> list = new ArrayList<>();
//        database.ifPresent(s -> list.add(parameter("database", s)));
//        return list.toArray(new Parameter[0]);
//      }
//    });
//  }
//
//}
