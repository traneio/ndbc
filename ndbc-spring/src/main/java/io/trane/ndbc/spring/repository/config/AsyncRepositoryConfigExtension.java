package io.trane.ndbc.spring.repository.config;

import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;

public class AsyncRepositoryConfigExtension extends RepositoryConfigurationExtensionSupport {

  @Override
  public String getRepositoryFactoryBeanClassName() {
    return null;
  }

  @Override
  protected String getModulePrefix() {
    return "NDBC";
  }
}
