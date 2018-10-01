package io.trane.ndbc.sqlserver.proto.unmarshaller;

import java.util.Optional;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.Unmarshaller;
import io.trane.ndbc.sqlserver.proto.Message.PreLoginResponse;
import io.trane.ndbc.sqlserver.proto.TDS;
import io.trane.ndbc.sqlserver.proto.marshaller.Util;

public class PreLoginResponseUnmarshaller implements Unmarshaller<PreLoginResponse> {

  @Override
  public Optional<PreLoginResponse> apply(final BufferReader b) {
    System.out.println(b.readByte());

    final byte[] preloginResponse = new byte[TDS.INITIAL_PACKET_SIZE];
    final String preloginErrorLogString = " Prelogin error";

    final byte requestedEncryptionLevel = TDS.ENCRYPT_INVALID;

    // Read the entire prelogin response
    int responseLength = preloginResponse.length;
    int responseBytesRead = 0;
    boolean processedResponseHeader = false;
    while (responseBytesRead < responseLength) {
      int bytesRead;

      try {
        // bytesRead = tdsChannel.read(preloginResponse, responseBytesRead,
        // responseLength - responseBytesRead);
        bytesRead = b.readableBytes();
      } catch (final Exception e) {
        System.out.println(toString() + preloginErrorLogString + " Error reading prelogin response: " + e.getMessage());
        throw e;
      }

      // If we reached EOF before the end of the prelogin response then something is
      // wrong.
      //
      // Special case: If there was no response at all (i.e. the server closed the
      // connection),
      // then maybe we are just trying to talk to an older server that doesn't support
      // prelogin
      // (and that we don't support with this driver).
      if (-1 == bytesRead)
        System.out.println(toString() + preloginErrorLogString + " Unexpected end of prelogin response after "
            + responseBytesRead + " bytes read");

      // Otherwise, we must have read some bytes...
      assert bytesRead >= 0;
      assert bytesRead <= responseLength - responseBytesRead;

      responseBytesRead += bytesRead;

      // Validate the response header if we haven't already done so and
      // we've read enough of the response to do it.
      if (!processedResponseHeader && responseBytesRead >= TDS.PACKET_HEADER_SIZE) {
        // Verify that the response is actually a response...
        if (TDS.PKT_REPLY != preloginResponse[0])
          System.out.println(toString() + preloginErrorLogString + " Unexpected response type:" + preloginResponse[0]);

        // Verify that the response claims to only be one TDS packet long.
        // In theory, it can be longer, but in current practice it isn't, as all of the
        // prelogin response items easily fit into a single 4K packet.
        if (TDS.STATUS_BIT_EOM != (TDS.STATUS_BIT_EOM & preloginResponse[1]))
          System.out
              .println(toString() + preloginErrorLogString + " Unexpected response status:" + preloginResponse[1]);

        // Verify that the length of the response claims to be small enough to fit in
        // the allocated area
        responseLength = Util.readUnsignedShortBigEndian(preloginResponse, 2);
        assert responseLength >= 0;

        if (responseLength >= preloginResponse.length)
          System.out.println(toString() + preloginErrorLogString + " Response length:" + responseLength
              + " is greater than allowed length:" + preloginResponse.length);

        processedResponseHeader = true;
      }
    }

    // Walk the response for prelogin options received. We expect at least to get
    // back the server version and the encryption level.
    boolean receivedVersionOption = false;
    byte negotiatedEncryptionLevel = TDS.ENCRYPT_INVALID;

    int responseIndex = TDS.PACKET_HEADER_SIZE;
    while (true) {
      // Get the option token
      if (responseIndex >= responseLength) {
        System.out.println(toString() + " Option token not found");
        throwInvalidTDS();
      }
      final byte optionToken = preloginResponse[responseIndex++];

      // When we reach the option terminator, we're done processing option tokens
      if (TDS.B_PRELOGIN_OPTION_TERMINATOR == optionToken)
        break;

      // Get the offset and length that follows the option token
      if (responseIndex + 4 >= responseLength) {
        System.out.println(toString() + " Offset/Length not found for option:" + optionToken);
        throwInvalidTDS();
      }

      final int optionOffset = Util.readUnsignedShortBigEndian(preloginResponse, responseIndex)
          + TDS.PACKET_HEADER_SIZE;
      responseIndex += 2;
      assert optionOffset >= 0;

      final int optionLength = Util.readUnsignedShortBigEndian(preloginResponse, responseIndex);
      responseIndex += 2;
      assert optionLength >= 0;

      if (optionOffset + optionLength > responseLength) {
        System.out.println(toString() + " Offset:" + optionOffset + " and length:" + optionLength
            + " exceed response length:" + responseLength);
        throwInvalidTDS();
      }

      switch (optionToken) {
      case TDS.B_PRELOGIN_OPTION_VERSION:
        if (receivedVersionOption) {
          System.out.println(toString() + " Version option already received");
          throwInvalidTDS();
        }

        if (6 != optionLength) {
          System.out
              .println(toString() + " Version option length:" + optionLength + " is incorrect.  Correct value is 6.");
          throwInvalidTDS();
        }

        final int serverMajorVersion = preloginResponse[optionOffset];
        if (serverMajorVersion < 9)
          System.out.println(
              toString() + " Server major version:" + serverMajorVersion + " is not supported by this driver.");

        System.out.println(toString() + " Server returned major version:" + preloginResponse[optionOffset]);

        receivedVersionOption = true;
        break;

      case TDS.B_PRELOGIN_OPTION_ENCRYPTION:
        if (TDS.ENCRYPT_INVALID != negotiatedEncryptionLevel) {
          System.out.println(toString() + " Encryption option already received");
          throwInvalidTDS();
        }

        if (1 != optionLength) {
          System.out.println(
              toString() + " Encryption option length:" + optionLength + " is incorrect.  Correct value is 1.");
          throwInvalidTDS();
        }

        negotiatedEncryptionLevel = preloginResponse[optionOffset];

        // If the server did not return a valid encryption level, terminate the
        // connection.
        if (TDS.ENCRYPT_OFF != negotiatedEncryptionLevel && TDS.ENCRYPT_ON != negotiatedEncryptionLevel
            && TDS.ENCRYPT_REQ != negotiatedEncryptionLevel && TDS.ENCRYPT_NOT_SUP != negotiatedEncryptionLevel) {
          System.out.println(toString() + " Server returned " + TDS.getEncryptionLevel(negotiatedEncryptionLevel));
          throwInvalidTDS();
        }

        System.out
            .println(toString() + " Negotiated encryption level:" + TDS.getEncryptionLevel(negotiatedEncryptionLevel));

        // If we requested SSL encryption and the server does not support it, then
        // terminate the connection.
        if (TDS.ENCRYPT_ON == requestedEncryptionLevel && TDS.ENCRYPT_ON != negotiatedEncryptionLevel
            && TDS.ENCRYPT_REQ != negotiatedEncryptionLevel)
          System.out.println("R_sslRequiredNoServerSupport");

        // If we say we don't support SSL and the server doesn't accept unencrypted
        // connections,
        // then terminate the connection.
        if (TDS.ENCRYPT_NOT_SUP == requestedEncryptionLevel && TDS.ENCRYPT_NOT_SUP != negotiatedEncryptionLevel) {
          // If the server required an encrypted connection then terminate with an
          // appropriate error.
          if (TDS.ENCRYPT_REQ == negotiatedEncryptionLevel)
            System.out.println("R_sslRequiredByServer");
          // terminate(SQLServerException.DRIVER_ERROR_SSL_FAILED,
          // SQLServerException.getErrString("R_sslRequiredByServer"));

          System.out.println(toString() + " Client requested encryption level: "
              + TDS.getEncryptionLevel(requestedEncryptionLevel) + " Server returned unexpected encryption level: "
              + TDS.getEncryptionLevel(negotiatedEncryptionLevel));
          throwInvalidTDS();
        }
        break;

      case TDS.B_PRELOGIN_OPTION_FEDAUTHREQUIRED:
        // Only 0x00 and 0x01 are accepted values from the server.
        if (0 != preloginResponse[optionOffset] && 1 != preloginResponse[optionOffset])
          System.out
              .println(toString() + " Server sent an unexpected value for FedAuthRequired PreLogin Option. Value was "
                  + preloginResponse[optionOffset]);

        // We must NOT use the response for the FEDAUTHREQUIRED PreLogin option, if the
        // connection string
        // option
        // was not using the new Authentication keyword or in other words, if
        // Authentication=NotSpecified
        // Or AccessToken is not null, mean token based authentication is used.
        // if (null != authenticationString
        // &&
        // !authenticationString.equalsIgnoreCase(SqlAuthentication.NotSpecified.toString())
        // || null != accessTokenInByte)
        // fedAuthRequiredPreLoginResponse = preloginResponse[optionOffset] == 1;
        break;

      default:
        System.out.println(toString() + " Ignoring prelogin response option:" + optionToken);
        break;
      }
    }

    if (!receivedVersionOption || TDS.ENCRYPT_INVALID == negotiatedEncryptionLevel) {
      System.out.println(toString() + " Prelogin response is missing version and/or encryption option.");
      throwInvalidTDS();
    }

    return null;
  }

  private void throwInvalidTDS() {
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
