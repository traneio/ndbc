package io.trane.ndbc.spring.repository.config;

import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;

import io.trane.ndbc.spring.repository.support.NdbcRepositoryFactoryBean;

public class NdbcRepositoryConfigExtension extends RepositoryConfigurationExtensionSupport {

  @Override
  public String getRepositoryFactoryBeanClassName() {
    return NdbcRepositoryFactoryBean.class.getName();
  }

  @Override
  protected String getModulePrefix() {
    return "ndbc";
  }
}
