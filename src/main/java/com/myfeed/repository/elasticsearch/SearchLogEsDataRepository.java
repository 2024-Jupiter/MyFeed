package com.myfeed.repository.elasticsearch;

import com.myfeed.model.elastic.SearchLogEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchLogEsDataRepository extends ElasticsearchRepository<SearchLogEs, String> {

}
