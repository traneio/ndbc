package io.trane.ndbc.postgres.proto.unmarshaller;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

import io.trane.ndbc.postgres.proto.Message.CommandComplete;
import io.trane.ndbc.postgres.proto.Message.CommandComplete.CopyComplete;
import io.trane.ndbc.postgres.proto.Message.CommandComplete.DeleteComplete;
import io.trane.ndbc.postgres.proto.Message.CommandComplete.FetchComplete;
import io.trane.ndbc.postgres.proto.Message.CommandComplete.InsertComplete;
import io.trane.ndbc.postgres.proto.Message.CommandComplete.MoveComplete;
import io.trane.ndbc.postgres.proto.Message.CommandComplete.SelectorOrCreateTableAsComplete;
import io.trane.ndbc.postgres.proto.Message.CommandComplete.UnknownCommandComplete;
import io.trane.ndbc.postgres.proto.Message.CommandComplete.UpdateComplete;
import io.trane.ndbc.proto.BufferReader;

final class CommandCompleteUnmarshaller {

  public final CommandComplete decode(final BufferReader b) {
    final String string = b.readCString();
    final String[] words = string.split(" ");
    switch (words[0]) {
      case "INSERT":
        return new InsertComplete(parseLong(words[1]), parseInt(words[2]));
      case "DELETE":
        return new DeleteComplete(parseLong(words[1]));
      case "UPDATE":
        return new UpdateComplete(parseLong(words[1]));
      case "SELECT":
        return new SelectorOrCreateTableAsComplete(parseLong(words[1]));
      case "MOVE":
        return new MoveComplete(parseLong(words[1]));
      case "FETCH":
        return new FetchComplete(parseLong(words[1]));
      case "COPY":
        return new CopyComplete(parseLong(words[1]));
      case "CREATE TABLE":
        return new SelectorOrCreateTableAsComplete(parseLong(words[1]));
      default:
        return new UnknownCommandComplete(0, string);
    }
  }
}
