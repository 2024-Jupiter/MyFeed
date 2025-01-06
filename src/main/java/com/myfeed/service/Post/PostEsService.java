package com.myfeed.service.Post;

import com.myfeed.model.post.*;
import com.myfeed.model.reply.ReplyDetailDto;
import com.myfeed.repository.elasticsearch.PostEsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostEsService {
    public static final int PAGE_SIZE = 10;
    @Autowired private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired private PostEsRepository postEsRepository;

    // 게시글 생성 (비동기 동기화)
    @Async
    public void syncToElasticsearch(PostEs postEs) {
        postEsRepository.save(postEs);
    }

    // 게시글 삭제
    @Async
    public void deleteFromElasticsearch(String id) {
        postEsRepository.deleteById(id);
    }

    // 댓글 작성 & 수정
    @Async
    public void updateToElasticsearch(PostEs postEs) {
        // 업데이트 요청
        UpdateQuery updateQuery = UpdateQuery.builder(postEs.getId())
                .withScript( "if (ctx._source.replies == null) { ctx._source.replies = [] }")
                .build();

        // 업데이트 실행
        UpdateResponse updateResponse = elasticsearchTemplate.update(updateQuery);

        if (updateResponse.getResult() == UpdateResponse.Result.UPDATED) {
            System.out.println("Replies updated successfully for ID: " + postEs.getId());
        } else {
            System.out.println("No update performed for ID: " + postEs.getId());
        }
    }

    // 댓글 삭제
    @Async
    public void deleteToElasticsearch(PostEs postEs, String replyIdToDelete) {
        // 삭제할 댓글 ID를 기반으로 댓글을 제거하는 스크립트 작성
        String script = String.format(
                "if (ctx._source.replies != null) { " +
                        "  ctx._source.replies.removeIf(reply -> reply.id == '%s'); " +  // 댓글 ID가 일치하는 댓글을 삭제
                        "}",
                replyIdToDelete
        );

        // 업데이트 요청
        UpdateQuery updateQuery = UpdateQuery.builder(postEs.getId())
                .withScript(script)  // 댓글을 삭제하는 스크립트
                .build();

        // 업데이트 실행
        UpdateResponse updateResponse = elasticsearchTemplate.update(updateQuery);

        if (updateResponse.getResult() == UpdateResponse.Result.UPDATED) {
            System.out.println("Reply deleted successfully for ID: " + postEs.getId());
        } else {
            System.out.println("No update performed for ID: " + postEs.getId());
        }
    }

    // 조회수 증가
    @Async
    public void incrementPostEsViewCountById(String id) {
        // 업데이트 요청 생성
        UpdateQuery updateQuery = UpdateQuery.builder(id)
                .withScript("ctx._source.viewCount += 1")
                .build();

        // 업데이트 실행
        UpdateResponse updateResponse = elasticsearchTemplate.update(updateQuery);

        if (updateResponse.getResult() == UpdateResponse.Result.UPDATED) {
            System.out.println("View count incremented successfully for ID: " + id);
        } else {
            System.out.println("No update performed for ID: " + id);
        }
    }

    // 좋아요 증가
    @Async
    public void incrementPostEsLikeCountById(String id) {
        // 업데이트 요청 생성
        UpdateQuery updateQuery = UpdateQuery.builder(id)
                .withScript("ctx._source.likeCount += 1")
                .build();

        // 업데이트 실행
        UpdateResponse updateResponse = elasticsearchTemplate.update(updateQuery);

        if (updateResponse.getResult() == UpdateResponse.Result.UPDATED) {
            System.out.println("Like count incremented successfully for ID: " + id);
        } else {
            System.out.println("No update performed for ID: " + id);
        }
    }

    // 좋아요 감소
    @Async
    public void decrementPostEsLikeCountById(String id) {
        // 업데이트 요청 생성
        UpdateQuery updateQuery = UpdateQuery.builder(id)
                .withScript("if (ctx._source.likeCount > 0) ctx._source.likeCount -= 1")
                .build();

        // 업데이트 실행
        UpdateResponse updateResponse = elasticsearchTemplate.update(updateQuery);

        if (updateResponse.getResult() == UpdateResponse.Result.UPDATED) {
            System.out.println("Like count decremented successfully for ID: " + id);
        } else {
            System.out.println("No update performed for ID: " + id);
        }
    }

    // 차단된 게시글 차단 처리 & 삭제된 유저 게시글 안 보이게
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

    // 자연어 처리 필요
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
