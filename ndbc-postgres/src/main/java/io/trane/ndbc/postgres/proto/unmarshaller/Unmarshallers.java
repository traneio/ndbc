package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;

public final class Unmarshallers {

  public final AuthenticationRequestUnmarshaller authenticationRequest;
  public final BackendKeyDataUnmarshaller        backendKeyData;
  public final BindCompleteUnmarshaller          bindComplete;
  public final CloseCompleteUnmarshaller         closeComplete;
  public final CommandCompleteUnmarshaller       commandComplete;
  public final CopyDataUnmarshaller              copyData;
  public final CopyDoneUnmarshaller              copyDone;
  public final DataRowUnmarshaller               dataRow;
  public final EmptyQueryResponseUnmarshaller    emptyQueryResponse;
  public final NoDataUnmarshaller                noData;
  public final ParameterDescriptionUnmarshaller  parameterDescription;
  public final ParameterStatusUnmarshaller       parameterStatus;
  public final ParseCompleteUnmarshaller         parseComplete;
  public final ReadyForQueryUnmarshaller         readyForQuery;
  public final RowDescriptionUnmarshaller        rowDescription;
  public final SSLResponseUnmarshaller           sslResponse;

  public Unmarshallers(final Charset charset) {
    this.authenticationRequest = new AuthenticationRequestUnmarshaller(charset);
    this.backendKeyData = new BackendKeyDataUnmarshaller(charset);
    this.bindComplete = new BindCompleteUnmarshaller(charset);
    this.closeComplete = new CloseCompleteUnmarshaller(charset);
    this.commandComplete = new CommandCompleteUnmarshaller(charset);
    this.copyData = new CopyDataUnmarshaller(charset);
    this.copyDone = new CopyDoneUnmarshaller(charset);
    this.dataRow = new DataRowUnmarshaller(charset);
    this.emptyQueryResponse = new EmptyQueryResponseUnmarshaller(charset);
    this.noData = new NoDataUnmarshaller(charset);
    this.parameterDescription = new ParameterDescriptionUnmarshaller(charset);
    this.parameterStatus = new ParameterStatusUnmarshaller(charset);
    this.parseComplete = new ParseCompleteUnmarshaller(charset);
    this.readyForQuery = new ReadyForQueryUnmarshaller(charset);
    this.rowDescription = new RowDescriptionUnmarshaller(charset);
    this.sslResponse = new SSLResponseUnmarshaller();
  }

}
