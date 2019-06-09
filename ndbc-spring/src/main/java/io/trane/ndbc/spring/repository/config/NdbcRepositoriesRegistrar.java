package io.trane.ndbc.spring.repository.config;

import java.lang.annotation.Annotation;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

public class NdbcRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

  @Override
  protected Class<? extends Annotation> getAnnotation() {
    return EnableNdbcRepositories.class;
  }

  @Override
  protected RepositoryConfigurationExtension getExtension() {
    return new NdbcRepositoryConfigExtension();
  }
}
