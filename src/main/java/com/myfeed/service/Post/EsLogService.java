package com.myfeed.service.Post;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.myfeed.model.elastic.SearchLogEs;
import com.myfeed.repository.elasticsearch.SearchLogEsDataRepository;
import com.myfeed.service.Post.record.KeywordCount;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EsLogService {

    @Autowired private SearchLogEsDataRepository searchLogEsDataRepository;
    @Autowired private ElasticsearchClient esClient;

    // 검색 로그 저장
    public void saveSearchLog(String userId, String searchWord) {
        System.out.println("===========saveSearchLog==========");
        try {
            SearchLogEs log = SearchLogEs.builder()
                .userId(userId)
                .searchText(searchWord)
                .createAt(LocalDateTime.now().toString())
                .build();

            // esClient를 사용하여 직접 저장
            IndexRequest<SearchLogEs> request = IndexRequest.of(i -> i
                .index("user_search_logs")  // 인덱스 이름
                .id(log.getId())      // document ID
                .document(log)
            );

            IndexResponse response = esClient.index(request);
            System.out.println("Document indexed successfully. ID: " + response.id());

        } catch (Exception e) {
            System.err.println("Error saving search log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveSearchLog2(String userId, String searchWord) {
        System.out.println("===========saveSearchLog==========");

    }
    // 1. 전체 기간 상위 검색어
    public List<KeywordCount> findAllTimeTopKeywords() throws IOException {
        System.out.println("===========findAllTimeTopKeywords==========");
        SearchRequest request = SearchRequest.of(builder -> builder
            .index("user_search_logs")
            .size(0)
            .aggregations("top_keywords",
                Aggregation.of(a -> a
                    .terms(TermsAggregation.of(t -> t
                        .field("keyword")
                        .size(3)
                    ))
                )
            )
        );

        SearchResponse<Void> response = esClient.search(request, Void.class);
        return parseKeywordAggregation(response, "top_keywords");
    }
    // 1. 내가 검색한 모든 시간 상위 검색어 3개
    public List<KeywordCount> findMyAllTimeTopKeywords(String userId) throws IOException {
        SearchRequest request = SearchRequest.of(builder -> builder
            .index("user_search_logs")  // 실제 인덱스 이름으로 변경 필요
            .size(0)
            .query(q -> q
                .bool(b -> b
                    .should(s -> s
                        .term(t -> t
                            .field("userId")
                            .value(userId)
                        )
                    )
                )
            )
            .aggregations("top_keywords",
                Aggregation.of(a -> a
                    .terms(TermsAggregation.of(t -> t
                        .field("keyword")
                        .size(3)
                    ))
                )
            )
        );

        SearchResponse<Void> response = esClient.search(request, Void.class);
        return parseKeywordAggregation(response, "top_keywords");
    }
    // 2. 일일 상위 검색어
    public List<KeywordCount> findDailyTopKeywords() throws IOException {
        ZonedDateTime oneDayAgo = ZonedDateTime.now().minusDays(1);
        System.out.println("oneDayAgo: " + oneDayAgo);
        SearchRequest request = getSearchRequestPopularSearchLogsByPeriod(oneDayAgo);

        SearchResponse<Void> response = esClient.search(request, Void.class);
        System.out.println("response: " + response);
        return parseKeywordAggregation(response, "top_keywords");
    }
    // 3. 주간 상위 검색어
    public List<KeywordCount> findWeeklyTopKeywords() throws IOException {
        ZonedDateTime oneWeekAgo = ZonedDateTime.now().minusWeeks(1);

        SearchRequest request = getSearchRequestPopularSearchLogsByPeriod(oneWeekAgo);

        SearchResponse<Void> response = esClient.search(request, Void.class);
        return parseKeywordAggregation(response, "top_keywords");
    }

    // 4. 월간 상위 검색어
    public List<KeywordCount> findMonthlyTopKeywords() throws IOException {
        ZonedDateTime oneMonthAgo = ZonedDateTime.now().minusMonths(1);

        SearchRequest request = getSearchRequestPopularSearchLogsByPeriod(oneMonthAgo);

        SearchResponse<Void> response = esClient.search(request, Void.class);
        return parseKeywordAggregation(response, "top_keywords");
    }

    // 일일, 주간, 월간 검색어 집계를 위한 검색 요청 생성
    // 인기 검색 로그 기간 조회
    public SearchRequest getSearchRequestPopularSearchLogsByPeriod(ZonedDateTime from) {
        // from ~ 현재 : ( 2025-01-01 ~ 현재의 데이터)
        System.out.println("데이트 :" + from.toString());
        return SearchRequest.of(builder -> builder
            .index("user_search_logs")
            .size(0)
            .query(Query.of(q -> q
                .range(r -> r
                    .date(d -> d
                        .field("timestamp")
                        .gte(from.toString())
                        .format("strict_date_optional_time||epoch_millis")
                        .timeZone("Asia/Seoul")
                    )
                )
            ))
//            .aggregations("top_keywords",
//                Aggregation.of(a -> a
//                    .terms(TermsAggregation.of(t -> t
//                        .field("keyword")
//                        .size(3)
//                    ))
//                )
//            )
        );
    }

    // 집계 결과 파싱 유틸리티 메서드
    private List<KeywordCount> parseKeywordAggregation(SearchResponse<Void> response, String aggregationName) {
        return response.aggregations()
            .get(aggregationName)
            .sterms()
            .buckets()
            .array()
            .stream()
            .map(bucket -> new KeywordCount(bucket.key().stringValue(), bucket.docCount()))
            .toList();
    }
}

