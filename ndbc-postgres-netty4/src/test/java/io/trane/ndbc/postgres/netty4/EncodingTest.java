package io.trane.ndbc.postgres.netty4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.value.Value;

public class EncodingTest extends TestEnv {

  private PreparedStatement insert = PreparedStatement.apply("INSERT INTO test VALUES (?)");
  private PreparedStatement select = PreparedStatement.apply("SELECT c FROM test");

  @Before
  public void recreateSchema() throws CheckedFutureException {
    ds.execute("DROP TABLE IF EXISTS test").get(timeout);
  }

  // @Test
  // public void bigDecimal() throws CheckedFutureException {
  // createTable("numeric");
  // BigDecimal value = new BigDecimal(1);
  //
  // execute(insert.bind(value));
  //
  // assertEquals(query(select).getBigDecimal(), value);
  // }

  @Test
  public void _boolean() throws CheckedFutureException {
    createTable("bool");
    Boolean value = true;

    execute(insert.bind(value));

    assertEquals(query(select).getBoolean(), value);
  }

  @Test
  public void byteArray() throws CheckedFutureException {
    createTable("bytea");
    byte[] value = "string".getBytes();

    execute(insert.bind(value));

    assertArrayEquals(query(select).getByteArray(), value);
  }

  @Test
  public void _double() throws CheckedFutureException {
    createTable("float8");
    Double value = 1D;

    execute(insert.bind(value));

    assertEquals(query(select).getDouble(), value);
  }

  @Test
  public void _float() throws CheckedFutureException {
    createTable("float4");
    Float value = 1.1F;

    execute(insert.bind(value));

    assertEquals(query(select).getFloat(), value);
  }

  @Test
  public void integer() throws CheckedFutureException {
    createTable("int4");
    Integer value = 1;

    execute(insert.bind(value));

    assertEquals(query(select).getInteger(), value);
  }

  @Test
  public void localDate() throws CheckedFutureException {
    createTable("date");
    LocalDate value = LocalDate.now();

    execute(insert.bind(value));

    assertEquals(query(select).getLocalDate(), value);
  }

  // @Test
  // public void localDateTime() throws CheckedFutureException {
  // createTable("timestamp");
  // LocalDateTime value = LocalDateTime.now();
  //
  // execute(insert.bind(value));
  //
  // assertEquals(query(select).getLocalDateTime(), value);
  // }

  @Test
  public void localTime() throws CheckedFutureException {
    createTable("time");
    LocalTime value = LocalTime.now();

    execute(insert.bind(value));

    assertEquals(query(select).getLocalTime(), value);
  }

  @Test
  public void _long() throws CheckedFutureException {
    createTable("int8");
    Long value = 1L;

    execute(insert.bind(value));

    assertEquals(query(select).getLong(), value);
  }

//  @Test
//  public void offsetTime() throws CheckedFutureException {
//    createTable("timetz");
//    OffsetTime value = OffsetTime.now();
//
//    execute(insert.bind(value));
//
//    assertEquals(query(select).getOffsetTime(), value);
//  }

  @Test
  public void _short() throws CheckedFutureException {
    createTable("int2");
    Short value = 1;

    execute(insert.bind(value));

    assertEquals(query(select).getShort(), value);
  }

  // Oid.TEXT, Oid.NAME, Oid.VARCHAR, Oid.XML, Oid.JSON, Oid.BPCHAR

  private void stringTest(String columnType, String value) throws CheckedFutureException {
    createTable(columnType);
    execute(insert.bind(value));

    assertEquals(query(select).getString(), value);
  }

  @Test
  public void stringText() throws CheckedFutureException {
    stringTest("text", "some text");
  }

  @Test
  public void stringName() throws CheckedFutureException {
    stringTest("name", "some name");
  }

  @Test
  public void stringVarchar() throws CheckedFutureException {
    stringTest("varchar", "some varchar");
  }

  @Test
  public void stringXml() throws CheckedFutureException {
    stringTest("varchar", "<someXml/>");
  }

  @Test
  public void stringJson() throws CheckedFutureException {
    stringTest("varchar", "{ some: \"json\" }");
  }

  @Test
  public void stringBpChar() throws CheckedFutureException {
    stringTest("varchar", "a");
  }

  private void createTable(String columnType) throws CheckedFutureException {
    ds.execute("CREATE TABLE test (c " + columnType + ")").get(timeout);
  }

  private void execute(PreparedStatement ps) throws CheckedFutureException {
    ds.execute(ps).get(timeout);
  }

  private Value<?> query(PreparedStatement ps) throws CheckedFutureException {
    Iterator<Row> it = ds.query(ps).get(timeout).iterator();
    Row row = it.next();
    assertFalse(it.hasNext());
    return row.column(0);
  }

}
