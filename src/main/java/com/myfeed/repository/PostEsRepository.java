package com.myfeed.repository;

import com.myfeed.model.elastic.post.PostEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostEsRepository extends ElasticsearchRepository<PostEs, String> {
    Page<PostEs> findAll(Pageable pageable);
}
