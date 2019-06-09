package io.trane.ndbc.spring.repository.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;

import io.trane.ndbc.spring.repository.support.NdbcRepositoryFactoryBean;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(NdbcRepositoriesRegistrar.class)
public @interface EnableNdbcRepositories {

  String[] value() default {};

  String[] basePackages() default {};

  Class<?>[] basePackageClasses() default {};

  Filter[] includeFilters() default {};

  Filter[] excludeFilters() default {};

  Class<?> repositoryFactoryBeanClass() default NdbcRepositoryFactoryBean.class;

  String namedQueriesLocation() default "";

  String repositoryImplementationPostfix() default "Impl";
}