package com.myfeed.service.Post;

import com.myfeed.model.post.Category;
import com.myfeed.model.post.Image;
import com.myfeed.model.post.Post;
import com.myfeed.model.user.Role;
import com.myfeed.model.user.User;
import com.myfeed.repository.PostRepository;
import com.myfeed.repository.UserRepository;
import com.myfeed.sync.PostSyncEvent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired PostRepository postRepository;
    @Autowired UserRepository userRepository;
    @Autowired private ApplicationEventPublisher eventPublisher;

    @Transactional
    public Post createOrUpdatePost(Post post) {
        Post savedPost = postRepository.save(post);
        eventPublisher.publishEvent(new PostSyncEvent(savedPost.getId(), "CREATE_OR_UPDATE"));

        return savedPost;
    }

    @Transactional
    public void deletePost(long id) {
        postRepository.deleteById(id);
        eventPublisher.publishEvent(new PostSyncEvent(id, "DELETE"));
    }

    @Transactional
    public Post incrementViews(long id) {
        Post post = postRepository.findById(id).orElse(null);
        post.setViewCount(post.getViewCount() + 1);
        return postRepository.save(post);
    }

    @Transactional
    public Post incrementLikes(long id) {
        Post post = postRepository.findById(id).orElse(null);
        post.setLikeCount(post.getLikeCount() + 1);
        return postRepository.save(post);
    }

    @Transactional
    public Post decrementLikes(long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post.getLikeCount() > 0) {
            post.setLikeCount(post.getLikeCount() - 1);
        } else {
            post.setLikeCount(0);
        }
        return postRepository.save(post);
    }

    // 게시글 작성
    @Override
    public Post findByPid(long pid) {
        return postRepository.findById(pid).orElse(null);
    }

    // 내 게시글 페이지네이션
    @Override
    public Page<Post> getMyPostList(int page, long uid) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return postRepository.findByUserUid(uid, pageable);
    }

    // 게시글의 사용자 아이디 가져오기
    @Override
    public List<User> getByUserUid(long uid) {
        return postRepository.findByUserUid(uid);
    }

    /*
    // 게시글 작성 (관리자 만 뉴스 글 작성 가능)
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

            return postRepository.save(post);
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

            return postRepository.save(post);
        }
    }

    // 게시글 수정
    @Override
    public void updatePost(Post post) {
        postRepository.save(post);
    }

    // 게시글 삭제
    @Override
    public void deletePost(long pid) {
        postRepository.deleteById(pid);
    }

    // 조회수 증가
    @Override
    public void incrementViewCount(long pid) {
        postRepository.incrementViewCount(pid);
    }

    // 좋아요 증가
    @Override
    public void incrementLikeCount(long pid) {
        postRepository.incrementLikeCount(pid);
    }

    // 좋아요 감소
    @Override
    public void decrementLikeCount(long pid) {
        postRepository.decrementLikeCount(pid);
    }
     */
}
