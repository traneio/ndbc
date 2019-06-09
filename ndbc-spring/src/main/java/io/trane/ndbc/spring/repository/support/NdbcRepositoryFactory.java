package io.trane.ndbc.spring.repository.support;

import org.springframework.data.jdbc.core.SimpleNdbcRepository;
import org.springframework.data.jdbc.core.SqlGeneratorSource;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

public class NdbcRepositoryFactory extends RepositoryFactorySupport {

  private final DataSource<PreparedStatement, Row> dataSource;
  private final SqlGeneratorSource sqlGeneratorSource;
  private final RelationalMappingContext context;

  public NdbcRepositoryFactory(final DataSource<PreparedStatement, Row> dataSource,
      final SqlGeneratorSource sqlGeneratorSource, final RelationalMappingContext context) {
    this.dataSource = dataSource;
    this.sqlGeneratorSource = sqlGeneratorSource;
    this.context = context;
  }

  @Override
  public <T, ID> EntityInformation<T, ID> getEntityInformation(final Class<T> domainClass) {
    return null;
  }

  @Override
  protected Object getTargetRepository(final RepositoryInformation metadata) {
    final RelationalPersistentEntity<?> entity = context.getPersistentEntity(metadata.getDomainType());
    return new SimpleNdbcRepository<>(dataSource, sqlGeneratorSource, entity);
  }

  @Override
  protected Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata) {
    return SimpleNdbcRepository.class;
  }
}
