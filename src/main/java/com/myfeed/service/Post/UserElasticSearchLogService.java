package com.myfeed.service.Post;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import java.time.Instant;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserElasticSearchLogService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public void  saveSearchLog(String userId, String keyword) {
        // 검색 로그 저장
        try {
            elasticsearchClient.index(i -> i
                    .index("user_search_logs")
                    .document(Map.of(
                    "userId", userId,
                    "keyword", keyword,
                    "timestamp", Instant.now().toString()
                ))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
