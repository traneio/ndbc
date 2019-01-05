package io.trane.ndbc.spring.repository.config;

import java.lang.annotation.Annotation;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

public class AsyncRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

  @Override
  protected Class<? extends Annotation> getAnnotation() {
    return EnableAsyncRepositories.class;
  }

  @Override
  protected RepositoryConfigurationExtension getExtension() {
    return null;
  }
}
