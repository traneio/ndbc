package io.trane.ndbc.mysql.proto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface Message {

	interface ServerMessage extends io.trane.ndbc.proto.ServerMessage, Message {
	}

	interface ClientMessage extends io.trane.ndbc.proto.ClientMessage, Message {
	}

	public static class HandshakeResponseMessage implements ClientMessage {
		final public int sequence;
		final public String username;
		final public Optional<String> password;
		final public Optional<String> database;
		final public String encoding;
		final public byte[] seed;
		final public String authenticationMethod;

		public HandshakeResponseMessage(final int sequence, final String username, final Optional<String> password,
				final Optional<String> database, final String encoding, final byte[] seed,
				final String authenticationMethod) {
			this.sequence = sequence;
			this.username = username;
			this.password = password;
			this.database = database;
			this.encoding = encoding;
			this.seed = seed;
			this.authenticationMethod = authenticationMethod;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			HandshakeResponseMessage that = (HandshakeResponseMessage) o;

			if (!username.equals(that.username))
				return false;
			if (!password.equals(that.password))
				return false;
			if (!database.equals(that.database))
				return false;
			if (!encoding.equals(that.encoding))
				return false;
			if (!Arrays.equals(seed, that.seed))
				return false;
			return authenticationMethod.equals(that.authenticationMethod);
		}

		@Override
		public int hashCode() {
			int result = username.hashCode();
			result = 31 * result + password.hashCode();
			result = 31 * result + database.hashCode();
			result = 31 * result + encoding.hashCode();
			result = 31 * result + Arrays.hashCode(seed);
			result = 31 * result + authenticationMethod.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "HandshakeResponseMessage{" + "sequence=" + sequence + ", username='" + username + '\''
					+ ", password=" + password + ", database=" + database + ", encoding='" + encoding + '\'' + ", seed="
					+ Arrays.toString(seed) + ", authenticationMethod='" + authenticationMethod + '\'' + '}';
		}
	}

	public static class InitialHandshakeMessage implements ServerMessage {

		final public int sequence;
		final public int protocolVersion;
		final public String serverVersion;
		final public long connectionId;
		final public byte[] seed;
		final public int serverCapabilites;
		final public int characterSet;
		final public int statusFlag;
		final public String authenticationMethod;

		public InitialHandshakeMessage(final int sequence, final int protocolVersion, final String serverVersion,
				final long connectionId, final byte[] seed, final int serverCapabilites, final int characterSet,
				final int statusFlag, final String authenticationMethod) {
			this.sequence = sequence;
			this.protocolVersion = protocolVersion;
			this.serverVersion = serverVersion;
			this.connectionId = connectionId;
			this.seed = seed;
			this.serverCapabilites = serverCapabilites;
			this.characterSet = characterSet;
			this.statusFlag = statusFlag;
			this.authenticationMethod = authenticationMethod;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			InitialHandshakeMessage that = (InitialHandshakeMessage) o;

			if (protocolVersion != that.protocolVersion)
				return false;
			if (connectionId != that.connectionId)
				return false;
			if (serverCapabilites != that.serverCapabilites)
				return false;
			if (characterSet != that.characterSet)
				return false;
			if (statusFlag != that.statusFlag)
				return false;
			if (!serverVersion.equals(that.serverVersion))
				return false;
			if (!Arrays.equals(seed, that.seed))
				return false;
			return authenticationMethod.equals(that.authenticationMethod);
		}

		@Override
		public int hashCode() {
			int result = protocolVersion;
			result = 31 * result + serverVersion.hashCode();
			result = 31 * result + (int) (connectionId ^ (connectionId >>> 32));
			result = 31 * result + Arrays.hashCode(seed);
			result = 31 * result + serverCapabilites;
			result = 31 * result + characterSet;
			result = 31 * result + statusFlag;
			result = 31 * result + authenticationMethod.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "InitialHandshakeMessage{" + "protocolVersion=" + protocolVersion + ", serverVersion='"
					+ serverVersion + '\'' + ", connectionId=" + connectionId + ", seed=" + Arrays.toString(seed)
					+ ", serverCapabilites=" + serverCapabilites + ", characterSet=" + characterSet + ", statusFlag="
					+ statusFlag + ", authenticationMethod='" + authenticationMethod + '\'' + '}';
		}

	}

	public static interface ServerResponseMessage extends ServerMessage {

	}

	public static class OkResponseMessage implements ServerResponseMessage {
		public long affectedRows;
		public long insertId;
		public int serverStatus;
		public int warningCount;
		public String message;

		public OkResponseMessage(final long affectedRows, final long insertId, final int serverStatus,
				final int warningCount, final String message) {
			this.affectedRows = affectedRows;
			this.insertId = insertId;
			this.serverStatus = serverStatus;
			this.warningCount = warningCount;
			this.message = message;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			OkResponseMessage that = (OkResponseMessage) o;

			if (affectedRows != that.affectedRows)
				return false;
			if (insertId != that.insertId)
				return false;
			if (serverStatus != that.serverStatus)
				return false;
			if (warningCount != that.warningCount)
				return false;
			return message.equals(that.message);
		}

		@Override
		public int hashCode() {
			int result = (int) (affectedRows ^ (affectedRows >>> 32));
			result = 31 * result + (int) (insertId ^ (insertId >>> 32));
			result = 31 * result + serverStatus;
			result = 31 * result + warningCount;
			result = 31 * result + message.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "OkResponseMessage{" + "affectedRows=" + affectedRows + ", insertId=" + insertId + ", serverStatus="
					+ serverStatus + ", warningCount=" + warningCount + ", message='" + message + '\'' + '}';
		}
	}

	public static class ErrorResponseMessage implements ServerResponseMessage {
		public String errorMessage;

		public ErrorResponseMessage(final String errorMessage) {
			this.errorMessage = errorMessage;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			ErrorResponseMessage that = (ErrorResponseMessage) o;

			return errorMessage != null ? errorMessage.equals(that.errorMessage) : that.errorMessage == null;
		}

		@Override
		public int hashCode() {
			return errorMessage != null ? errorMessage.hashCode() : 0;
		}

		@Override
		public String toString() {
			return "ErrorResponseMessage{" + "errorMessage='" + errorMessage + '\'' + '}';
		}
	}

	public static class EofResponseMessage implements ServerResponseMessage {
		public int warnings;
		public int serverStatus;

		public EofResponseMessage(final int warnings, final int serverStatus) {
			this.warnings = warnings;
			this.serverStatus = serverStatus;
		}
	}

	public static interface Command extends ClientMessage {

	}

	public static class NoCommand implements Command {

	}

	public static interface TextCommand extends Command {
		public byte getCommand();

		public String getSqlStatement();
	}

	public static class PrepareStatementCommand implements TextCommand {
		private final byte command = (byte) 0x16;
		private final String sqlStatement;

		public PrepareStatementCommand(final String sqlStatement) {
			this.sqlStatement = sqlStatement;
		}

		@Override
		public byte getCommand() {
			return command;
		}

		@Override
		public String getSqlStatement() {
			return sqlStatement;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			PrepareStatementCommand that = (PrepareStatementCommand) o;

			if (command != that.command)
				return false;
			return sqlStatement.equals(that.sqlStatement);
		}

		@Override
		public int hashCode() {
			int result = command;
			result = 31 * result + sqlStatement.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "PrepareStatementCommand{" + "command=" + command + ", sqlStatement='" + sqlStatement + '\'' + '}';
		}
	}

	public static class OkPrepareStatement implements ServerResponseMessage {
		public final long statementId;
		private final int numOfColumns;
		private final int numOfParameters;
		private final int warningCount;

		public OkPrepareStatement(final long statementId, final int numOfColumns, final int numOfParameters,
				final int warningCount) {
			this.statementId = statementId;
			this.numOfColumns = numOfColumns;
			this.numOfParameters = numOfParameters;
			this.warningCount = warningCount;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			OkPrepareStatement that = (OkPrepareStatement) o;

			if (statementId != that.statementId)
				return false;
			if (numOfColumns != that.numOfColumns)
				return false;
			if (numOfParameters != that.numOfParameters)
				return false;
			if (warningCount != that.warningCount)
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			int result = (int) (statementId ^ (statementId >>> 32));
			result = 31 * result + (numOfColumns ^ (numOfColumns >>> 32));
			result = 31 * result + numOfParameters;
			result = 31 * result + warningCount;
			return result;
		}

		@Override
		public String toString() {
			return "OkPrepareStatement{" + "statementId=" + statementId + ", numOfColumns=" + numOfColumns
					+ ", numOfParameters=" + numOfParameters + ", warningsCount=" + warningCount + '}';
		}
	}

	public static class ExecuteStatementCommand implements Command {
		public final byte command = (byte) 0x17;
		public final long statementId;
		public final byte flags = (byte) 0x01; // CURSOR_TYPE_READ_ONLY
		public final int iterationCount = 1;

		public ExecuteStatementCommand(final long statementId) {
			this.statementId = statementId;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			ExecuteStatementCommand that = (ExecuteStatementCommand) o;

			if (command != that.command)
				if (statementId != that.statementId)
					return false;
			return true;
		}

		@Override
		public int hashCode() {
			int result = command;
			result = (int) (31 * result + statementId);
			return result;
		}

		@Override
		public String toString() {
			return "ExecuteStatementCommand{" + "command=" + command + ", statementId=" + statementId + ", flags="
					+ flags + ", iterationCount=" + iterationCount + '}';
		}
	}

	public static class CloseStatementCommand implements Command {
		public final byte command = (byte) 0x19;
		public final long statementId;

		public CloseStatementCommand(final long statementId) {
			this.statementId = statementId;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			ExecuteStatementCommand that = (ExecuteStatementCommand) o;

			if (command != that.command)
				if (statementId != that.statementId)
					return false;
			return true;
		}

		@Override
		public int hashCode() {
			int result = command;
			result = (int) (31 * result + statementId);
			return result;
		}

		@Override
		public String toString() {
			return "CloseStatementCommand{" + "command=" + command + ", statementId=" + statementId + '}';
		}
	}

	public static class QueryCommand implements TextCommand {
		private final byte command = (byte) 0x03;
		private final String sqlStatement;

		public QueryCommand(final String sqlStatement) {
			this.sqlStatement = sqlStatement;
		}

		@Override
		public byte getCommand() {
			return command;
		}

		@Override
		public String getSqlStatement() {
			return sqlStatement;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			QueryCommand that = (QueryCommand) o;

			if (command != that.command)
				return false;
			return sqlStatement.equals(that.sqlStatement);
		}

		@Override
		public int hashCode() {
			int result = command;
			result = 31 * result + sqlStatement.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "QueryCommand{" + "command=" + command + ", sqlStatement='" + sqlStatement + '\'' + '}';
		}
	}

	public static class StatementCommand implements TextCommand {
		private final byte command = (byte) 0x03;
		private final String sqlStatement;

		public StatementCommand(final String sqlStatement) {
			this.sqlStatement = sqlStatement;
		}

		@Override
		public byte getCommand() {
			return command;
		}

		@Override
		public String getSqlStatement() {
			return sqlStatement;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			StatementCommand that = (StatementCommand) o;

			if (command != that.command)
				return false;
			return sqlStatement.equals(that.sqlStatement);
		}

		@Override
		public int hashCode() {
			int result = command;
			result = 31 * result + sqlStatement.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "StatementCommand{" + "command=" + command + ", sqlStatement='" + sqlStatement + '\'' + '}';
		}
	}

	public static class Field implements ServerMessage {
		public String catalog;
		public String db;
		public String table;
		public String origTable;
		public String name;
		public String origName;
		public int charset;
		public long displayLength;
		public int fieldType;
		public int flags;
		public int decimals;

		public Field(final String catalog, final String db, final String table, final String origTable,
				final String name, final String origName, final int charset, final long displayLength,
				final int fieldType, final int flags, final int decimals) {
			this.catalog = catalog;
			this.db = db;
			this.table = table;
			this.origTable = origTable;
			this.name = name;
			this.origName = origName;
			this.charset = charset;
			this.displayLength = displayLength;
			this.fieldType = fieldType;
			this.flags = flags;
			this.decimals = decimals;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			Field field = (Field) o;

			if (charset != field.charset)
				return false;
			if (displayLength != field.displayLength)
				return false;
			if (fieldType != field.fieldType)
				return false;
			if (flags != field.flags)
				return false;
			if (decimals != field.decimals)
				return false;
			if (!catalog.equals(field.catalog))
				return false;
			if (!db.equals(field.db))
				return false;
			if (!table.equals(field.table))
				return false;
			if (!origTable.equals(field.origTable))
				return false;
			if (!name.equals(field.name))
				return false;
			return origName.equals(field.origName);
		}

		@Override
		public int hashCode() {
			int result = catalog.hashCode();
			result = 31 * result + db.hashCode();
			result = 31 * result + table.hashCode();
			result = 31 * result + origTable.hashCode();
			result = 31 * result + name.hashCode();
			result = 31 * result + origName.hashCode();
			result = 31 * result + charset;
			result = 31 * result + (int) (displayLength ^ (displayLength >>> 32));
			result = 31 * result + fieldType;
			result = 31 * result + flags;
			result = 31 * result + decimals;
			return result;
		}

		@Override
		public String toString() {
			return "Field{" + "catalog='" + catalog + '\'' + ", db='" + db + '\'' + ", table='" + table + '\''
					+ ", origTable='" + origTable + '\'' + ", name='" + name + '\'' + ", origName='" + origName + '\''
					+ ", charset=" + charset + ", displayLength=" + displayLength + ", fieldType=" + fieldType
					+ ", flags=" + flags + ", decimals=" + decimals + '}';
		}
	}

	public static class TextRow implements ServerMessage {
		public List<String> values;

		public TextRow(final List<String> values) {
			this.values = values;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			TextRow textRow = (TextRow) o;

			return values.equals(textRow.values);
		}

		@Override
		public int hashCode() {
			return values.hashCode();
		}

		@Override
		public String toString() {
			return "TextRow{" + "values=" + values + '}';
		}
	}

	public static class ResultSet implements ServerMessage {
		public List<TextRow> textRows;
		public List<Field> fields;

		public ResultSet(final List<Field> fields, final List<TextRow> textRows) {
			this.textRows = textRows;
			this.fields = fields;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			ResultSet resultSet = (ResultSet) o;

			if (!textRows.equals(resultSet.textRows))
				return false;
			return fields.equals(resultSet.fields);
		}

		@Override
		public int hashCode() {
			int result = textRows.hashCode();
			result = 31 * result + fields.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "ResultSet{" + "textRows=" + textRows + ", fields=" + fields + '}';
		}
	}
}
