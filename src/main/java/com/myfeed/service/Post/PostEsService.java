package com.myfeed.service.Post;

import com.myfeed.model.post.PostEsDto;
import com.myfeed.model.post.PostEs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostEsService {
    public static final int PAGE_SIZE = 10;
    @Autowired private ElasticsearchTemplate elasticsearchTemplate;

    // 자연어 처리 필요
    // 차단된 게시글 안보이게
    public Page<PostEsDto> getPagedPosts(int page, String field, String keyword, String sortField, String sortDirection) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        // 정렬 필드에 keyword 서브 필드 사용
        String sortFieldToUse = sortField + ".keyword";
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Query query = NativeQuery.builder()
                .withQuery(buildMatchQuery(field, keyword))
                .withSort(Sort.by(Sort.Order.desc("_score")))
                .withSort(Sort.by(direction, sortFieldToUse))
                .withTrackScores(true)
                .withPageable(pageable)
                .build();
        SearchHits<PostEs> searchHits = elasticsearchTemplate.search(query, PostEs.class);
        List<PostEsDto> postEsDtoList = searchHits
                .getSearchHits()
                .stream()
                .map(hit -> new PostEsDto(hit.getContent(), hit.getScore()))
                .toList();

        long totalHits = searchHits.getTotalHits();
        return new PageImpl<>(postEsDtoList, pageable, totalHits);
    }

    private Query buildMatchQuery(String field, String keyword) {
        if (keyword.isEmpty()) {
            return new StringQuery("{\"match_all\": {}}");
        }
        String queryString = String.format("""
                        {
                            "match": {
                                "%s": {
                                    "query": "%s",
                                    "fuzziness": "AUTO"
                                }
                            }
                        }        
                """,
                field, keyword
        );
        return new StringQuery(queryString);
    }
}
