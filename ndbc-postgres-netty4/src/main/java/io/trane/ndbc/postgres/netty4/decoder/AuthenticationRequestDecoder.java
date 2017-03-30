package io.trane.ndbc.postgres.netty4.decoder;

import io.netty.buffer.ByteBuf;
import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest;
import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest.*;

class AuthenticationRequestDecoder {

  private final AuthenticationOk authenticationOk = new AuthenticationOk();
  private final AuthenticationKerberosV5 authenticationKerberosV5 = new AuthenticationKerberosV5();
  private final AuthenticationCleartextPassword authenticationCleartextPassword = new AuthenticationCleartextPassword();
  private final AuthenticationSCMCredential authenticationSCMCredential = new AuthenticationSCMCredential();
  private final AuthenticationGSS authenticationGSS = new AuthenticationGSS();
  private final AuthenticationSSPI authenticationSSPI = new AuthenticationSSPI();

  public final AuthenticationRequest decode(ByteBuf buf) {
    switch (buf.readInt()) {

    case 0:
      return authenticationOk;
    case 2:
      return authenticationKerberosV5;
    case 3:
      return authenticationCleartextPassword;
    case 5:
      byte[] salt = new byte[4];
      buf.readBytes(salt);
      return new AuthenticationMD5Password(salt);
    case 6:
      return authenticationSCMCredential;
    case 7:
      return authenticationGSS;
    case 9:
      return authenticationSSPI;
    case 8:
      byte[] authenticationData = new byte[buf.readableBytes()];
      buf.readBytes(authenticationData);
      new AuthenticationGSSContinue(authenticationData);
    }
    return null;
  }

}
