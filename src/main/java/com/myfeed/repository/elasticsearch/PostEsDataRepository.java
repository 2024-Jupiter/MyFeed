package com.myfeed.repository.elasticsearch;

import com.myfeed.model.elastic.PostEsDto1;
import com.myfeed.model.elastic.post.PostEs;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostEsDataRepository extends ElasticsearchRepository<PostEs, String> {
    // 페이지네이션
    Page<PostEs> findAll(Pageable pageable);

    // 1. 제목 검색
    Page<PostEs> findByTitleContaining(String keyword, Pageable pageable);

    // 2. 내용 검색
    Page<PostEs> findByContentContaining(String keyword , Pageable pageable);

    // 3. 제목+내용 검색 (OR 조건)
    Page<PostEs> findByTitleContainingOrContentContaining(String title, String content  , Pageable pageable);

    // 4. 제목+내용 검색 (AND 조건)
    Page<PostEs> findByTitleContainingAndContentContaining(String title, String content , Pageable pageable);

    Page<PostEsDto1> findByTitleOrContent(String keyword1,String keyword2,Pageable pageRequest);

}
