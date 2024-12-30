package com.myfeed.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
public class ElasticsearchConfig {

	@Value("${spring.elasticsearch.rest.url}")
	private String elasticsearchUrl;

	@Value("${spring.elasticsearch.rest.username}")
	private String username;

	@Value("${spring.elasticsearch.rest.password}")
	private String password;

	@Bean
	public ElasticsearchClient getElasticsearchClient() {
		RestClientBuilder builder = org.elasticsearch.client.RestClient.builder(HttpHost.create(elasticsearchUrl));

		builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
			// 사용자 이름과 비밀번호를 설정
			final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
			return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
		});

		// RestClient 및 ElasticsearchClient 생성
		RestClient restClient = builder.build();
		RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
		return new ElasticsearchClient(transport);
	}
}

// @Configuration
// public class ElasticsearchConfig extends ElasticsearchConfiguration {
// 	@Value("${spring.elasticsearch.rest.username}")
// 	private String username;
//
// 	@Value("${spring.elasticsearch.rest.password}")
// 	private String password;
//
// 	@Value("${spring.elasticsearch.rest.url}")
// 	private String esHost;
//
// 	@Override
// 	public ClientConfiguration clientConfiguration() {
// 		return ClientConfiguration.builder()
// 			.connectedTo(esHost)
// 			.usingSsl() // ssl 사용
// 			.withBasicAuth(username, password)
// 			.build();
// 	}
// }
