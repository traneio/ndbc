package io.trane.ndbc.postgres.netty4.decoder;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

public class Read {

  public static byte[] bytes(ByteBuf buf, int length) {
    byte[] bytes = new byte[length];
    buf.readBytes(bytes);
    return bytes;
  }

  public static byte[] bytes(ByteBuf buf) {
    return bytes(buf, buf.readableBytes());
  }

  public static String string(Charset charset, ByteBuf buf, int length) {
    byte[] b = new byte[length];
    buf.readBytes(b);
    String string = new String(b, charset);
    return string;
  }

  public static String string(Charset charset, ByteBuf buf) {
    return string(charset, buf, buf.readableBytes());
  }

}
