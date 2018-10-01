package io.trane.ndbc.sqlserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class Main {

  private Socket socket;
  private final DataOutputStream out;
  private final DataInputStream in;

  public Main() throws IOException {
    final Socket socket = new Socket("localhost", 1433);
    out = new DataOutputStream(socket.getOutputStream());
    in = new DataInputStream(socket.getInputStream());
  }

  public static void main(final String[] args) throws IOException {
    final Main main = new Main();

    final byte[] bytes = new byte[] { 0x12, 0x01, 0x00, 0x43, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x10, 0x00, 0x06,
        0x01, 0x00, 0x16, 0x00, 0x01, 0x05, 0x00, 0x17, 0x00, 0x24, (byte) 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, (byte) 0x9A, (byte) 0xF5, (byte) 0xE4, (byte) 0x8F, (byte) 0xAF, (byte) 0xD1, 0x2E, 0x48, (byte) 0xB2,
        0x17, 0x0B, (byte) 0xC1, 0x70, (byte) 0xA7, 0x32, (byte) 0x8E, 0x27, (byte) 0xDF, 0x53, (byte) 0x8D,
        (byte) 0xD8, (byte) 0xB4, 0x2D, 0x46, (byte) 0x8E, (byte) 0xA7, (byte) 0x98, (byte) 0xF1, 0x71, 0x37, 0x0E,
        0x5E, 0x01, 0x00, 0x00, 0x00 };

    main.out.write(bytes);
    main.out.flush();

    final byte[] result = new byte[4096];
    System.out.println(main.in.read(result));
    System.out.println(Arrays.toString(result));

    final byte list[][] = new byte[8][];
    final byte data[][] = new byte[8][];
    int recordCount = 0;
    byte record[] = new byte[5];

    // Read entry pointers
    record[0] = (byte) main.in.read();
    while ((record[0] & 0xFF) != 0xFF) {
      if (recordCount == list.length)
        throw new IOException("Pre Login packet has more than 8 entries");
      // Read record
      main.in.read(record, 1, 4);
      list[recordCount++] = record;
      record = new byte[5];
      record[0] = (byte) main.in.read();
    }
    // Read entry data
    for (int i = 0; i < recordCount; i++) {
      final byte value[] = new byte[list[i][4]];
      main.in.read(value);
      data[i] = value;
    }
    // Diagnostic dump
    System.out.println("PreLogin server response");
    for (int i = 0; i < recordCount; i++)
      System.out.println("Record " + i + " = " + Arrays.toString(data[i]));

    if (recordCount > 1)
      System.out.println(data[1][0]); // This is the server side SSL mode
    else
      System.out.println(0x2);
  }
}
