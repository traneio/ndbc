package io.trane.ndbc.spring.repository.support;

import java.io.Serializable;

import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.jdbc.core.SqlGeneratorSource;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;

import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

public class NdbcRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
    extends TransactionalRepositoryFactoryBeanSupport<T, S, ID> implements ApplicationEventPublisherAware {

  private final DataSource<PreparedStatement, Row> dataSource;
  private final SqlGeneratorSource sqlGeneratorSource;
  private final RelationalMappingContext mappingContext;

  protected NdbcRepositoryFactoryBean(final Class<? extends T> repositoryInterface,
      final DataSource<PreparedStatement, Row> dataSource, final SqlGeneratorSource sqlGeneratorSource,
      final RelationalMappingContext mappingContext) {
    super(repositoryInterface);
    this.dataSource = dataSource;
    this.sqlGeneratorSource = sqlGeneratorSource;
    super.setMappingContext(mappingContext);
    this.mappingContext = mappingContext;
  }

  @Override
  protected RepositoryFactorySupport doCreateRepositoryFactory() {
    return new NdbcRepositoryFactory(dataSource, sqlGeneratorSource, mappingContext);
  }

  @Override
  public void afterPropertiesSet() {
    super.afterPropertiesSet();
  }
}
