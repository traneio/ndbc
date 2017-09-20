package io.trane.ndbc.mysql.proto.unmarshaller;

import static io.trane.ndbc.mysql.proto.Message.*;

import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.proto.BufferReader;

import java.util.LinkedList;
import java.util.List;

public class TextResultSetUnmarshaller {


  public ResultSet decode(final BufferReader br) {
    PacketBufferReader packet = new PacketBufferReader(br);
    //int returnCode = packet.readByte();
    long numCols = packet.readVariableLong();
    List<Field> fields = new LinkedList<>();
    for (int i = 0; i < numCols; i++) {
      fields.add(decodeField(new PacketBufferReader(br)));
    }

    EofResponseMessage eof1 = ServerResponseUnmarshaller.decodeEof(new PacketBufferReader(br));

    List<TextRow> rows = new LinkedList<>();
    boolean eof = false;
    while(!eof) {
      PacketBufferReader rowPacket = new PacketBufferReader(br);
      rowPacket.markReaderIndex();
      if((rowPacket.readByte() & 0xFF) == ServerResponseUnmarshaller.EOF_BYTE) {
        rowPacket.resetReaderIndex();
        EofResponseMessage eof2 = ServerResponseUnmarshaller.decodeEof(rowPacket);
        eof = true;
      } else {
        rowPacket.resetReaderIndex();
        List<String> values = new LinkedList<>();
        for (int i = 0; i < numCols; i++) {
          values.add(new String(rowPacket.readLengthCodedBytes())); // TODO user correct charset
        }
        rows.add(new TextRow(values));
      }
    }
    return new ResultSet(fields, rows);
  }

  public Field decodeField(PacketBufferReader br) {
    byte[] bytesCatalog = br.readLengthCodedBytes();
    byte[] bytesDb = br.readLengthCodedBytes();
    byte[] bytesTable = br.readLengthCodedBytes();
    byte[] bytesOrigTable = br.readLengthCodedBytes();
    byte[] bytesName = br.readLengthCodedBytes();
    byte[] bytesOrigName = br.readLengthCodedBytes();
    br.readVariableLong(); // length of the following fields (always 0x0c)
    int charset = br.readUnsignedShort();
    String catalog = new String(bytesCatalog); // TODO consider charset
    String db = new String(bytesDb);
    String table = new String(bytesTable);
    String origTable = new String(bytesOrigTable);
    String name = new String(bytesName);
    String origName = new String(bytesOrigName);
    long length = br.readUnsignedInt();
    int fieldType = br.readByte() & 0xFF;
    int flags = br.readUnsignedShort();
    int decimals = br.readByte() & 0xFF;
    return new Field(catalog,
            db,
            table,
            origTable,
            name,
            origName,
            charset,
            length,
            fieldType,
            flags,
            decimals);
  }
}
