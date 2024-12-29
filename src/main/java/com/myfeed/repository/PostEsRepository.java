package com.myfeed.repository;

import com.myfeed.model.post.PostEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostEsRepository extends ElasticsearchRepository<PostEs, String> {
    // 페이지네이션
    Page<PostEs> findAll(Pageable pageable);
}
