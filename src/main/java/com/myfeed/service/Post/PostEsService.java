package com.myfeed.service.Post;

import com.myfeed.model.elastic.post.PostEs;
import com.myfeed.model.elastic.post.PostEsDto;
import com.myfeed.model.post.*;
import com.myfeed.repository.elasticsearch.PostEsRepository;
import com.myfeed.service.Post.crawlingdata.NewsExcelReader;
import com.myfeed.service.Post.crawlingdata.VelogDto;
import com.myfeed.service.Post.crawlingdata.VelogJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostEsService {
    public static final int PAGE_SIZE = 10;
    @Autowired private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired private PostEsRepository postEsRepository;

    // 생성, 수정, 조회수 & 좋아요 (비동기 동기화)
    @Async
    public void syncToElasticsearch(PostEs postEs) {
        postEsRepository.save(postEs);
    }

    // 삭제
    @Async
    public void deleteFromElasticsearch(String id) {
        postEsRepository.deleteById(id);
        System.out.println("Deleted from Elasticsearch: ID=" + id);
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

    // 게시글 검색
    public Page<PostEs> searchQuery(String query, int page) {
        Page<PostEs> result = postEsRepository.findByTitleOrContent(query, query, PageRequest.of(page - 1, PAGE_SIZE));
        return result;
    }

    @Async
    public void insertByJsonFile() {
        System.out.println("Inserting by JSON file...");
        List<VelogDto> velogDtos = new VelogJsonReader().loadJson();
        List<PostEs> list = velogDtos.stream().map(velogDto -> {
            return PostEs.builder()
                    .id(String.valueOf(velogDto.hashCode()))
                    .title(velogDto.getTitle())
                    .nickname(velogDto.getUserName())
                    .content(velogDto.getContent())
                    .category(Category.GENERAL)
                    .createAt(LocalDateTime.parse(velogDto.getDate()))
                    .likeCount(velogDto.getLikeCount())
                    .viewCount(0)
                    .build();
        }).toList();
        postEsRepository.saveAll(list);
        System.out.println("Successfully saved to Elasticsearch");
    }


}
