package io.trane.ndbc.sqlserver.proto.marshaller;

import java.util.UUID;

import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;
import io.trane.ndbc.sqlserver.proto.Message.PreLogin;
import io.trane.ndbc.sqlserver.proto.TDS;

public final class PreLoginMarshaller implements Marshaller<PreLogin> {

  @Override
  public void apply(final PreLogin msg, final BufferWriter b) {
    final boolean fedAuthRequiredByUser = false;
    byte requestedEncryptionLevel = TDS.ENCRYPT_OFF;

    // Message length (incl. header)
    final byte messageLength;
    final byte fedAuthOffset;
    if (fedAuthRequiredByUser) {
      messageLength = TDS.B_PRELOGIN_MESSAGE_LENGTH_WITH_FEDAUTH;
      requestedEncryptionLevel = TDS.ENCRYPT_ON;

      // since we added one more line for prelogin option with fedauth,
      // we also needed to modify the offsets above, by adding 5 to each offset,
      // since the data session of each option is push 5 bytes behind.
      fedAuthOffset = 5;
    } else {
      messageLength = TDS.B_PRELOGIN_MESSAGE_LENGTH;
      fedAuthOffset = 0;
    }

    final byte[] preloginRequest = new byte[messageLength];

    int preloginRequestOffset = 0;

    final byte[] bufferHeader = {
        // Buffer Header
        TDS.PKT_PRELOGIN, // Message Type
        TDS.STATUS_BIT_EOM, 0, messageLength, 0, 0, // SPID (not used)
        0, // Packet (not used)
        0, // Window (not used)
    };

    System.arraycopy(bufferHeader, 0, preloginRequest, preloginRequestOffset, bufferHeader.length);
    preloginRequestOffset = preloginRequestOffset + bufferHeader.length;

    final byte[] preloginOptionsBeforeFedAuth = {
        // OPTION_TOKEN (BYTE), OFFSET (USHORT), LENGTH (USHORT)
        TDS.B_PRELOGIN_OPTION_VERSION, 0, (byte) (16 + fedAuthOffset), 0, 6, // UL_VERSION + US_SUBBUILD
        TDS.B_PRELOGIN_OPTION_ENCRYPTION, 0, (byte) (22 + fedAuthOffset), 0, 1, // B_FENCRYPTION
        TDS.B_PRELOGIN_OPTION_TRACEID, 0, (byte) (23 + fedAuthOffset), 0, 36, // ClientConnectionId + ActivityId
    };
    System.arraycopy(preloginOptionsBeforeFedAuth, 0, preloginRequest, preloginRequestOffset,
        preloginOptionsBeforeFedAuth.length);
    preloginRequestOffset = preloginRequestOffset + preloginOptionsBeforeFedAuth.length;

    if (fedAuthRequiredByUser) {
      final byte[] preloginOptions2 = { TDS.B_PRELOGIN_OPTION_FEDAUTHREQUIRED, 0, 64, 0, 1, };
      System.arraycopy(preloginOptions2, 0, preloginRequest, preloginRequestOffset, preloginOptions2.length);
      preloginRequestOffset = preloginRequestOffset + preloginOptions2.length;
    }

    preloginRequest[preloginRequestOffset] = TDS.B_PRELOGIN_OPTION_TERMINATOR;
    preloginRequestOffset++;

    // PL_OPTION_DATA
    final byte[] preloginOptionData = {
        // - Server version -
        // (out param, filled in by the server in the prelogin response).
        0, 0, 0, 0, 0, 0,

        // - Encryption -
        requestedEncryptionLevel,

        // TRACEID Data Session (ClientConnectionId + ActivityId) - Initialize to 0
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
    System.arraycopy(preloginOptionData, 0, preloginRequest, preloginRequestOffset, preloginOptionData.length);
    preloginRequestOffset = preloginRequestOffset + preloginOptionData.length;

    // If the client’s PRELOGIN request message contains the FEDAUTHREQUIRED option,
    // the client MUST specify 0x01 as the B_FEDAUTHREQUIRED value
    if (fedAuthRequiredByUser) {
      preloginRequest[preloginRequestOffset] = 1;
      preloginRequestOffset = preloginRequestOffset + 1;
    }

    final UUID clientConnectionId = UUID.randomUUID();
    final ActivityId activityId = ActivityCorrelator.getNext();
    final byte[] actIdByteArray = Util.asGuidByteArray(activityId.getId());
    final byte[] conIdByteArray = Util.asGuidByteArray(clientConnectionId);

    int offset;

    if (fedAuthRequiredByUser)
      offset = preloginRequest.length - 36 - 1; // point to the TRACEID Data Session (one more byte for fedauth
                                                // data session)
    else
      offset = preloginRequest.length - 36; // point to the TRACEID Data Session

    // copy ClientConnectionId
    System.arraycopy(conIdByteArray, 0, preloginRequest, offset, conIdByteArray.length);
    offset += conIdByteArray.length;

    // copy ActivityId
    System.arraycopy(actIdByteArray, 0, preloginRequest, offset, actIdByteArray.length);
    offset += actIdByteArray.length;

    final long seqNum = activityId.getSequence();
    Util.writeInt((int) seqNum, preloginRequest, offset);
    offset += 4;

    // [18, 1, 0, 67, 0, 0, 0, 0, 0, 0, 16, 0, 6, 1, 0, 22, 0, 1, 5, 0, 23, 0, 36,
    // -1, 0, 0, 0, 0, 0, 0, 0, 57, -3, -37, 45, -16, 116, -78, 67, -72, -34, -36,
    // 69, -26, -3, 3, -48, -93, 57, 18, -19, -16, 90, -108, 76, -83, 49, -125, 40,
    // -10, -97, 56, -19, 1, 0, 0, 0]

    // [0x12, 0x01, 0x00, 0x43, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x10, 0x00,
    // 0x06, 0x01, 0x00, 0x16, 0x00, 0x01, 0x05, 0x00, 0x17, 0x00, 0x24, 0xFF, 0x00,
    // 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x9A, 0xF5, 0xE4, 0x8F, 0xAF, 0xD1, 0x2E,
    // 0x48, 0xB2, 0x17, 0x0B, 0xC1, 0x70, 0xA7, 0x32, 0x8E, 0x27, 0xDF, 0x53, 0x8D,
    // 0xD8, 0xB4, 0x2D, 0x46, 0x8E, 0xA7, 0x98, 0xF1, 0x71, 0x37, 0x0E, 0x5E, 0x01,
    // 0x00, 0x00, 0x00]

    b.writeByte((byte) 0x12);
    b.writeByte((byte) 0xFF);
  }
}