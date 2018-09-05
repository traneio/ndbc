package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest;
import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest.AuthenticationCleartextPassword;
import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest.AuthenticationGSS;
import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest.AuthenticationGSSContinue;
import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest.AuthenticationKerberosV5;
import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest.AuthenticationMD5Password;
import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest.AuthenticationOk;
import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest.AuthenticationSCMCredential;
import io.trane.ndbc.postgres.proto.Message.AuthenticationRequest.AuthenticationSSPI;
import io.trane.ndbc.proto.BufferReader;

public final class AuthenticationRequestUnmarshaller extends PostgresUnmarshaller<AuthenticationRequest> {

  private static final AuthenticationOk                authenticationOk                = new AuthenticationOk();
  private static final AuthenticationKerberosV5        authenticationKerberosV5        = new AuthenticationKerberosV5();
  private static final AuthenticationCleartextPassword authenticationCleartextPassword = new AuthenticationCleartextPassword();
  private static final AuthenticationSCMCredential     authenticationSCMCredential     = new AuthenticationSCMCredential();
  private static final AuthenticationGSS               authenticationGSS               = new AuthenticationGSS();
  private static final AuthenticationSSPI              authenticationSSPI              = new AuthenticationSSPI();

  public AuthenticationRequestUnmarshaller(final Charset charset) {
    super(charset);
  }

  @Override
  protected boolean acceptsType(final byte tpe) {
    return tpe == 'R';
  }

  @Override
  public final AuthenticationRequest decode(final byte tpe, final BufferReader b) {
    switch (b.readInt()) {
      case 0:
        return authenticationOk;
      case 2:
        return authenticationKerberosV5;
      case 3:
        return authenticationCleartextPassword;
      case 5:
        return new AuthenticationMD5Password(b.readBytes(4));
      case 6:
        return authenticationSCMCredential;
      case 7:
        return authenticationGSS;
      case 9:
        return authenticationSSPI;
      case 8:
        new AuthenticationGSSContinue(b.readBytes());
      default:
        throw new IllegalStateException("Invalid authentication request type");
    }
  }
}
