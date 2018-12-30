package io.trane.ndbc.postgres.netty4;

import java.util.Collection;

import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.reactivestreams.Publisher;
import org.reactivestreams.tck.PublisherVerification;

import io.trane.ndbc.DataSource;
import io.trane.ndbc.PostgresPreparedStatement;
import io.trane.ndbc.PostgresRow;
import io.trane.ndbc.Row;

public class StreamTest extends PublisherVerification<Row> {

  @Parameters(name = "{1}")
  public static Collection<Object[]> data() {
    return PostgresEnv.dataSources;
  }

  @Parameter(0)
  public DataSource<PostgresPreparedStatement, PostgresRow> ds;

  @Override
  public Publisher<Row> createPublisher(long elements) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Publisher<Row> createFailedPublisher() {
    // TODO Auto-generated method stub
    return null;
  }

}
