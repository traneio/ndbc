package io.trane.ndbc.postgres.proto.unmarshaller;

public final class Unmarshallers {

  public final AuthenticationRequestUnmarshaller authenticationRequest = new AuthenticationRequestUnmarshaller();
  public final BackendKeyDataUnmarshaller        backendKeyData        = new BackendKeyDataUnmarshaller();
  public final BindCompleteUnmarshaller          bindComplete          = new BindCompleteUnmarshaller();
  public final CloseCompleteUnmarshaller         closeComplete         = new CloseCompleteUnmarshaller();
  public final CommandCompleteUnmarshaller       commandComplete       = new CommandCompleteUnmarshaller();
  public final CopyDataUnmarshaller              copyData              = new CopyDataUnmarshaller();
  public final CopyDoneUnmarshaller              copyDone              = new CopyDoneUnmarshaller();
  public final DataRowUnmarshaller               dataRow               = new DataRowUnmarshaller();
  public final EmptyQueryResponseUnmarshaller    emptyQueryResponse    = new EmptyQueryResponseUnmarshaller();
  public final NoDataUnmarshaller                noData                = new NoDataUnmarshaller();
  public final ParameterDescriptionUnmarshaller  parameterDescription  = new ParameterDescriptionUnmarshaller();
  public final ParameterStatusUnmarshaller       parameterStatus       = new ParameterStatusUnmarshaller();
  public final ParseCompleteUnmarshaller         parseComplete         = new ParseCompleteUnmarshaller();
  public final ReadyForQueryUnmarshaller         readyForQuery         = new ReadyForQueryUnmarshaller();
  public final RowDescriptionUnmarshaller        rowDescription        = new RowDescriptionUnmarshaller();
  public final SSLResponseUnmarshaller           sslResponse           = new SSLResponseUnmarshaller();
}
