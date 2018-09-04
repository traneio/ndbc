package io.trane.ndbc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.trane.ndbc.value.BigDecimalArrayValue;
import io.trane.ndbc.value.BigDecimalValue;
import io.trane.ndbc.value.BooleanArrayValue;
import io.trane.ndbc.value.BooleanValue;
import io.trane.ndbc.value.ByteArrayArrayValue;
import io.trane.ndbc.value.ByteArrayValue;
import io.trane.ndbc.value.ByteValue;
import io.trane.ndbc.value.DoubleArrayValue;
import io.trane.ndbc.value.DoubleValue;
import io.trane.ndbc.value.FloatArrayValue;
import io.trane.ndbc.value.FloatValue;
import io.trane.ndbc.value.IntegerArrayValue;
import io.trane.ndbc.value.IntegerValue;
import io.trane.ndbc.value.LocalDateArrayValue;
import io.trane.ndbc.value.LocalDateTimeArrayValue;
import io.trane.ndbc.value.LocalDateTimeValue;
import io.trane.ndbc.value.LocalDateValue;
import io.trane.ndbc.value.LocalTimeArrayValue;
import io.trane.ndbc.value.LocalTimeValue;
import io.trane.ndbc.value.LongArrayValue;
import io.trane.ndbc.value.LongValue;
import io.trane.ndbc.value.OffsetTimeArrayValue;
import io.trane.ndbc.value.OffsetTimeValue;
import io.trane.ndbc.value.ShortArrayValue;
import io.trane.ndbc.value.ShortValue;
import io.trane.ndbc.value.StringArrayValue;
import io.trane.ndbc.value.StringValue;
import io.trane.ndbc.value.UUIDArrayValue;
import io.trane.ndbc.value.UUIDValue;
import io.trane.ndbc.value.Value;

public final class PreparedStatement {

	private static final Value<?>[] emptyValues = new Value<?>[0];

	public static final PreparedStatement apply(final String query) {
		return new PreparedStatement(query, emptyValues);
	}

	private final String query;
	private final Value<?>[] params;

	private PreparedStatement(final String query, final Value<?>[] params) {
		this.query = query;
		this.params = params;
	}

	public final PreparedStatement setBigDecimal(final BigDecimal value) {
		return setBigDecimal(params.length, value);
	}

	public final PreparedStatement setBigDecimal(final int index, final BigDecimal value) {
		return set(index, value == null ? Value.NULL : new BigDecimalValue(value));
	}

	public final PreparedStatement setBigDecimalArray(final BigDecimal[] value) {
		return setBigDecimalArray(params.length, value);
	}

	public final PreparedStatement setBigDecimalArray(final int index, final BigDecimal[] value) {
		return set(index, value == null ? Value.NULL : new BigDecimalArrayValue(value));
	}

	public final PreparedStatement setBoolean(final Boolean value) {
		return setBoolean(params.length, value);
	}

	public final PreparedStatement setBoolean(final int index, final Boolean value) {
		return set(index, value == null ? Value.NULL : new BooleanValue(value));
	}

	public final PreparedStatement setBooleanArray(final Boolean[] value) {
		return setBooleanArray(params.length, value);
	}

	public final PreparedStatement setBooleanArray(final int index, final Boolean[] value) {
		return set(index, value == null ? Value.NULL : new BooleanArrayValue(value));
	}

	public final PreparedStatement setByteArray(final byte[] value) {
		return setByteArray(params.length, value);
	}

	public final PreparedStatement setByteArray(final int index, final byte[] value) {
		return set(index, value == null ? Value.NULL : new ByteArrayValue(value));
	}

	public final PreparedStatement setByteArrayArray(final byte[][] value) {
		return setByteArrayArray(params.length, value);
	}

	public final PreparedStatement setByteArrayArray(final int index, final byte[][] value) {
		return set(index, value == null ? Value.NULL : new ByteArrayArrayValue(value));
	}

	public final PreparedStatement setDouble(final Double value) {
		return setDouble(params.length, value);
	}

	public final PreparedStatement setDouble(final int index, final Double value) {
		return set(index, value == null ? Value.NULL : new DoubleValue(value));
	}

	public final PreparedStatement setDoubleArray(final Double[] value) {
		return setDoubleArray(params.length, value);
	}

	public final PreparedStatement setDoubleArray(final int index, final Double[] value) {
		return set(index, value == null ? Value.NULL : new DoubleArrayValue(value));
	}

	public final PreparedStatement setFloat(final Float value) {
		return setFloat(params.length, value);
	}

	public final PreparedStatement setFloat(final int index, final Float value) {
		return set(index, value == null ? Value.NULL : new FloatValue(value));
	}

	public final PreparedStatement setFloatArray(final Float[] value) {
		return setFloatArray(params.length, value);
	}

	public final PreparedStatement setFloatArray(final int index, final Float[] value) {
		return set(index, value == null ? Value.NULL : new FloatArrayValue(value));
	}

	public final PreparedStatement setInteger(final Integer value) {
		return setInteger(params.length, value);
	}

	public final PreparedStatement setInteger(final int index, final Integer value) {
		return set(index, value == null ? Value.NULL : new IntegerValue(value));
	}

	public final PreparedStatement setIntegerArray(final Integer[] value) {
		return setIntegerArray(params.length, value);
	}

	public final PreparedStatement setIntegerArray(final int index, final Integer[] value) {
		return set(index, value == null ? Value.NULL : new IntegerArrayValue(value));
	}

	public final PreparedStatement setLocalDate(final LocalDate value) {
		return setLocalDate(params.length, value);
	}

	public final PreparedStatement setLocalDate(final int index, final LocalDate value) {
		return set(index, value == null ? Value.NULL : new LocalDateValue(value));
	}

	public final PreparedStatement setLocalDateArray(final LocalDate[] value) {
		return setLocalDateArray(params.length, value);
	}

	public final PreparedStatement setLocalDateArray(final int index, final LocalDate[] value) {
		return set(index, value == null ? Value.NULL : new LocalDateArrayValue(value));
	}

	public final PreparedStatement setLocalDateTime(final LocalDateTime value) {
		return setLocalDateTime(params.length, value);
	}

	public final PreparedStatement setLocalDateTime(final int index, final LocalDateTime value) {
		return set(index, value == null ? Value.NULL : new LocalDateTimeValue(value));
	}

	public final PreparedStatement setLocalDateTimeArray(final LocalDateTime[] value) {
		return setLocalDateTimeArray(params.length, value);
	}

	public final PreparedStatement setLocalDateTimeArray(final int index, final LocalDateTime[] value) {
		return set(index, value == null ? Value.NULL : new LocalDateTimeArrayValue(value));
	}

	public final PreparedStatement setLocalTime(final LocalTime value) {
		return setLocalTime(params.length, value);
	}

	public final PreparedStatement setLocalTime(final int index, final LocalTime value) {
		return set(index, value == null ? Value.NULL : new LocalTimeValue(value));
	}

	public final PreparedStatement setLocalTimeArray(final LocalTime[] value) {
		return setLocalTimeArray(params.length, value);
	}

	public final PreparedStatement setLocalTimeArray(final int index, final LocalTime[] value) {
		return set(index, value == null ? Value.NULL : new LocalTimeArrayValue(value));
	}

	public final PreparedStatement setLong(final Long value) {
		return setLong(params.length, value);
	}

	public final PreparedStatement setLong(final int index, final Long value) {
		return set(index, value == null ? Value.NULL : new LongValue(value));
	}

	public final PreparedStatement setLongArray(final Long[] value) {
		return setLongArray(params.length, value);
	}

	public final PreparedStatement setLongArray(final int index, final Long[] value) {
		return set(index, value == null ? Value.NULL : new LongArrayValue(value));
	}

	public final PreparedStatement setOffsetTime(final OffsetTime value) {
		return setOffsetTime(params.length, value);
	}

	public final PreparedStatement setOffsetTime(final int index, final OffsetTime value) {
		return set(value == null ? Value.NULL : new OffsetTimeValue(value));
	}

	public final PreparedStatement setOffsetTimeArray(final OffsetTime[] value) {
		return setOffsetTimeArray(params.length, value);
	}

	public final PreparedStatement setOffsetTimeArray(final int index, final OffsetTime[] value) {
		return set(index, value == null ? Value.NULL : new OffsetTimeArrayValue(value));
	}

	public final PreparedStatement setByte(final Byte value) {
		return setByte(params.length, value);
	}

	public final PreparedStatement setByte(final int index, final Byte value) {
		return set(index, value == null ? Value.NULL : new ByteValue(value));
	}

	public final PreparedStatement setShort(final Short value) {
		return setShort(params.length, value);
	}

	public final PreparedStatement setShort(final int index, final Short value) {
		return set(index, value == null ? Value.NULL : new ShortValue(value));
	}

	public final PreparedStatement setShortArray(final Short[] value) {
		return setShortArray(params.length, value);
	}

	public final PreparedStatement setShortArray(final int index, final Short[] value) {
		return set(index, value == null ? Value.NULL : new ShortArrayValue(value));
	}

	public final PreparedStatement setString(final String value) {
		return setString(params.length, value);
	}

	public final PreparedStatement setString(final int index, final String value) {
		return set(index, value == null ? Value.NULL : new StringValue(value));
	}

	public final PreparedStatement setStringArray(final String[] value) {
		return setStringArray(params.length, value);
	}

	public final PreparedStatement setStringArray(final int index, final String[] value) {
		return set(index, value == null ? Value.NULL : new StringArrayValue(value));
	}

	public final PreparedStatement setUUID(final int index, final UUID value) {
		return set(index, value == null ? Value.NULL : new UUIDValue(value));
	}

	public final PreparedStatement setUUID(final UUID value) {
		return setUUID(params.length, value);
	}

	public final PreparedStatement setUUIDArray(final UUID[] value) {
		return setUUIDArray(params.length, value);
	}

	public final PreparedStatement setUUIDArray(final int index, final UUID[] value) {
		return set(index, value == null ? Value.NULL : new UUIDArrayValue(value));
	}

	public final PreparedStatement setNull() {
		return setNull(params.length);
	}

	public final PreparedStatement setNull(final int index) {
		return set(index, Value.NULL);
	}

	public final PreparedStatement set(final Value<?> param) {
		return set(params.length, param);
	}

	public final PreparedStatement set(final int index, final Value<?> param) {
		if (index < 0)
			throw new IllegalArgumentException("PreparedStatement binding index can't be negative");
		final Value<?>[] newParams;
		if (index >= params.length) {
			newParams = Arrays.copyOf(params, index + 1);
			Arrays.fill(newParams, params.length, index, Value.NULL);
		} else
			newParams = Arrays.copyOf(params, params.length);
		newParams[index] = param;
		return new PreparedStatement(query, newParams);
	}

	public final String query() {
		return query;
	}

	public final List<Value<?>> params() {
		return Collections.unmodifiableList(Arrays.asList(params));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(params);
		result = prime * result + ((query == null) ? 0 : query.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PreparedStatement other = (PreparedStatement) obj;
		if (!Arrays.equals(params, other.params))
			return false;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		return true;
	}
}
