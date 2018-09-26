/*
 * Microsoft JDBC Driver for SQL Server Copyright(c) Microsoft Corporation All rights reserved. This program is made
 * available under the terms of the MIT License. See the LICENSE file in the project root for more information.
 */

package io.trane.ndbc.sqlserver.proto.marshaller;

import java.util.UUID;

/**
 * Various driver utilities.
 *
 */

public final class Util {
  final static String SYSTEM_SPEC_VERSION = System.getProperty("java.specification.version");
  final static char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  final static String WSIDNotAvailable = ""; // default string when WSID is not available

  final static String ActivityIdTraceProperty = "com.microsoft.sqlserver.jdbc.traceactivity";

  // The JRE is identified by the string below so that the driver can make
  // any vendor or version specific decisions
  static final String SYSTEM_JRE = System.getProperty("java.vendor") + " " + System.getProperty("java.version");

  public static void writeInt(final int value, final byte valueBytes[], final int offset) {
    valueBytes[offset + 0] = (byte) (value >> 0 & 0xFF);
    valueBytes[offset + 1] = (byte) (value >> 8 & 0xFF);
    valueBytes[offset + 2] = (byte) (value >> 16 & 0xFF);
    valueBytes[offset + 3] = (byte) (value >> 24 & 0xFF);
  }

  public static void writeIntBigEndian(final int value, final byte valueBytes[], final int offset) {
    valueBytes[offset + 0] = (byte) (value >> 24 & 0xFF);
    valueBytes[offset + 1] = (byte) (value >> 16 & 0xFF);
    valueBytes[offset + 2] = (byte) (value >> 8 & 0xFF);
    valueBytes[offset + 3] = (byte) (value >> 0 & 0xFF);
  }

  public static void writeLongBigEndian(final long value, final byte valueBytes[], final int offset) {
    valueBytes[offset + 0] = (byte) (value >> 56 & 0xFF);
    valueBytes[offset + 1] = (byte) (value >> 48 & 0xFF);
    valueBytes[offset + 2] = (byte) (value >> 40 & 0xFF);
    valueBytes[offset + 3] = (byte) (value >> 32 & 0xFF);
    valueBytes[offset + 4] = (byte) (value >> 24 & 0xFF);
    valueBytes[offset + 5] = (byte) (value >> 16 & 0xFF);
    valueBytes[offset + 6] = (byte) (value >> 8 & 0xFF);
    valueBytes[offset + 7] = (byte) (value >> 0 & 0xFF);
  }

  public static final byte[] asGuidByteArray(final UUID aId) {
    final long msb = aId.getMostSignificantBits();
    final long lsb = aId.getLeastSignificantBits();
    final byte[] buffer = new byte[16];
    Util.writeLongBigEndian(msb, buffer, 0);
    Util.writeLongBigEndian(lsb, buffer, 8);

    // For the first three fields, UUID uses network byte order,
    // Guid uses native byte order. So we need to reverse
    // the first three fields before sending to server.

    byte tmpByte;

    // Reverse the first 4 bytes
    tmpByte = buffer[0];
    buffer[0] = buffer[3];
    buffer[3] = tmpByte;
    tmpByte = buffer[1];
    buffer[1] = buffer[2];
    buffer[2] = tmpByte;

    // Reverse the 5th and the 6th
    tmpByte = buffer[4];
    buffer[4] = buffer[5];
    buffer[5] = tmpByte;

    // Reverse the 7th and the 8th
    tmpByte = buffer[6];
    buffer[6] = buffer[7];
    buffer[7] = tmpByte;

    return buffer;
  }

  public static int readUnsignedShortBigEndian(final byte data[], final int nOffset) {
    return (data[nOffset] & 0xFF) << 8 | data[nOffset + 1] & 0xFF;
  }
}
