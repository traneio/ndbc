package io.trane.ndbc.postgres.netty4.decoder;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.trane.ndbc.postgres.proto.Message.CommandComplete;
import io.trane.ndbc.postgres.proto.Message.CommandComplete.*;
import static java.lang.Integer.parseInt;

public class CommandCompleteDecoder {

  private final Charset charset;

  public CommandCompleteDecoder(Charset charset) {
    super();
    this.charset = charset;
  }

  public CommandComplete decode(ByteBuf buf) {
    String string = readString(buf);
    String[] words = string.split(" ");
    switch (words[0]) {
    case "INSERT":
      return new InsertComplete(parseInt(words[1]), parseInt(words[2]));
    case "DELETE":
      return new DeleteComplete(parseInt(words[1]));
    case "UPDATE":
      return new UpdateComplete(parseInt(words[1]));
    case "SELECT":
      return new SelectorOrCreateTableAsComplete(parseInt(words[1]));
    case "MOVE":
      return new MoveComplete(parseInt(words[1]));
    case "FETCH":
      return new FetchComplete(parseInt(words[1]));
    case "COPY":
      return new CopyComplete(parseInt(words[1]));
    default:
      throw new IllegalStateException("Invalid command complete string: " + string);
    }
  }

  private String readString(ByteBuf buf) {
    byte[] b = new byte[buf.readableBytes()];
    buf.readBytes(b);
    String string = new String(b, charset);
    return string;
  }
}
