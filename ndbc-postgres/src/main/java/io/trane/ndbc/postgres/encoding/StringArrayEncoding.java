package io.trane.ndbc.postgres.encoding;

import java.util.Set;

import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.StringArrayValue;

final class StringArrayEncoding extends ArrayEncoding<String, StringArrayValue> {

	private final StringEncoding stringEncoding;
	private final String[] emptyArray = new String[0];

	public StringArrayEncoding(StringEncoding stringEncoding) {
		this.stringEncoding = stringEncoding;
	}

	@Override
	public final Integer oid() {
		return Oid.VARCHAR_ARRAY;
	}

	@Override
	public final Set<Integer> additionalOids() {
		return Collections.toImmutableSet(Oid.NAME_ARRAY, Oid.TEXT_ARRAY, Oid.BPCHAR_ARRAY);
	}

	@Override
	public final Class<StringArrayValue> valueClass() {
		return StringArrayValue.class;
	}

	@Override
	protected String[] newArray(int length) {
		return new String[length];
	}

	@Override
	protected String[] emptyArray() {
		return emptyArray;
	}

	@Override
	protected Encoding<String, ?> itemEncoding() {
		return stringEncoding;
	}

	@Override
	protected StringArrayValue box(String[] value) {
		return new StringArrayValue(value);
	}

	@Override
	protected String[] unbox(StringArrayValue value) {
		return value.getStringArray();
	}
}
