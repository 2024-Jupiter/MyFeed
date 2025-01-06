package com.myfeed.repository.elasticsearch;

import com.myfeed.model.elastic.post.PostEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostEsRepository extends ElasticsearchRepository<PostEs, String> {
    // 페이지 네이션
    Page<PostEs> findAll(Pageable pageable);
    Page<PostEs> findByTitleOrContent(String title, String content, Pageable pageable);
}
