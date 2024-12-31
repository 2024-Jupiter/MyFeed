package com.myfeed.service.Post;

import com.myfeed.converter.PostConverter;
import com.myfeed.model.post.Category;
import com.myfeed.model.post.Image;
import com.myfeed.model.post.Post;
import com.myfeed.model.elastic.post.PostEs;
import com.myfeed.model.user.Role;
import com.myfeed.model.user.User;
import com.myfeed.repository.elasticsearch.PostEsRepository;
import com.myfeed.repository.jpa.PostRepository;
import com.myfeed.repository.jpa.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    @Autowired PostRepository postRepository;
    @Autowired PostEsRepository postEsRepository;
    @Autowired UserRepository userRepository;

    // 게시글 작성
    @Override
    public Post findByPid(long pid) {
        return postRepository.findById(pid).orElse(null);
    }

    // 게시글의 사용자 아이디 가져오기
    @Override
    public User getByUserUid(long uid) {
        return postRepository.findByUserId(uid);
    }

    // 게시글 작성 (관리자 만 뉴스 글 작성 가능)
    @Transactional
    @Override
    public Post createPost(long uid, Category category, String title, String content, String imgSrc) {
        User user = userRepository.findById(uid).orElse(null);

        if (user.getRole().equals(Role.ADMIN)) {
            category = Category.NEWS;
            Post post = Post.builder()
                    .user(user).category(category).title(title).content(content)
                    .viewCount(0).likeCount(0)
                    .build();

            Image image = Image.builder()
                    .post(post).imageSrc(imgSrc)
                    .build();
            post.addImage(image);

            Post savedPost = postRepository.save(post);
            PostEs postEs = PostConverter.toElasticsearchDocument(savedPost);
            postEsRepository.save(postEs);
            return savedPost;
        } else {
            category = Category.GENERAL;
            Post post = Post.builder()
                    .user(user).category(category).title(title).content(content)
                    .viewCount(0).likeCount(0)
                    .build();

            Image image = Image.builder()
                    .post(post).imageSrc(imgSrc)
                    .build();
            post.addImage(image);

            Post savedPost = postRepository.save(post);
            PostEs postEs = PostConverter.toElasticsearchDocument(savedPost);
            postEsRepository.save(postEs);
            return savedPost;
        }
    }

    // 내 게시글 페이지네이션
    @Override
    public Page<Post> getMyPostList(int page, long uid) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return postRepository.findByUserId(uid, pageable);
    }

    // 게시글 수정
    @Transactional
    @Override
    public void updatePost(Post post) {
        Post updatedPost = postRepository.save(post);
        PostEs postEs = PostConverter.toElasticsearchDocument(updatedPost);
        postEsRepository.save(postEs);
    }

    // 게시글 삭제
    @Transactional
    @Override
    public void deletePost(long pid) {
        postRepository.deleteById(pid);
        postEsRepository.deleteById(String.valueOf(pid));
    }

    // 조회수 증가
    @Override
    public void incrementViewCount(long pid) {
        postRepository.incrementViewCount(pid);
    }

    // 좋아요 증가
    @Override    public void incrementLikeCount(long pid) {
        postRepository.incrementLikeCount(pid);
    }


    // 좋아요 감소
    @Override
    public void decrementLikeCount(long pid) {
        postRepository.decrementLikeCount(pid);
    }
}
