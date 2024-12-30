package com.myfeed.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.myfeed.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "com.myfeed.repository.elasticsearch")
public class RepositoryConfig {
}
