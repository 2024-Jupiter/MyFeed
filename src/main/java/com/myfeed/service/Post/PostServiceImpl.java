package com.myfeed.service.Post;

import com.myfeed.model.post.Category;
import com.myfeed.model.post.Image;
import com.myfeed.model.post.Post;
import com.myfeed.model.user.Role;
import com.myfeed.model.user.User;
import com.myfeed.sync.PostSyncEvent;
import com.myfeed.repository.jpa.PostRepository;
import com.myfeed.repository.jpa.UserRepository;
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
    @Override
    public Post createOrUpdatePost(Post post) {
        Post savedPost = postRepository.save(post);
        eventPublisher.publishEvent(new PostSyncEvent(savedPost.getId(), "CREATE_OR_UPDATE"));

        return savedPost;
    }

    @Transactional
    @Override
    public void deletePostById(long id) {
        postRepository.deleteById(id);
        eventPublisher.publishEvent(new PostSyncEvent(id, "DELETE"));
    }

    // 조회수 증가 (동시성)
    @Transactional
    @Override
    public void incrementPostViewCountById(long id) {
        postRepository.updateViewCountById(id);
    }

    // 좋아요 증가 (동시성)
    @Transactional
    @Override
    public void incrementPostLikeCountById(long id) {
        postRepository.updateLikeCountById(id);
    }


    // 좋아요 감소 (동시성)
    @Transactional
    @Override
    public void decrementPostLikeCountById(long id) {
        postRepository.decrementLikeCountById(id);
    }

    // 게시글 작성
    @Override
    public Post findPostById(long id) {
        return postRepository.findById(id).orElse(null);
    }

    // 내 게시글 페이지네이션
    @Override
    public Page<Post> getPagedPostsByUserId(int page, long userId) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return postRepository.findPagedPostsByUserId(userId, pageable);
    }

    // 게시글의 사용자 아이디 가져오기
    @Override
    public List<User> getUsersById(long uid) {
        return postRepository.findUsersById(uid);
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

<<<<<<< HEAD
=======
    // 내 게시글 페이지네이션
    @Override
    public Page<Post> getMyPostList(int page, long uid) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return postRepository.findByUserId(uid, pageable);
    }

>>>>>>> 0b1e490525da3855b672b65c1c128e7ca8a1799e
    // 게시글 수정
    @Override
    public void updatePost(Post post) {
        postRepository.save(post);
    }

    @Transactional
    public Post incrementPostViewCountById(long id) {
        Post post = postRepository.findById(id).orElse(null);
        post.setViewCount(post.getViewCount() + 1);
        return postRepository.save(post);
    }

    @Transactional
    public Post incrementPostLikeCountById(long id) {
        Post post = postRepository.findById(id).orElse(null);
        post.setLikeCount(post.getLikeCount() + 1);
        return postRepository.save(post);
    }

    @Transactional
    public Post decrementPostLikeCountById(long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post.getLikeCount() > 0) {
            post.setLikeCount(post.getLikeCount() - 1);
        } else {
            post.setLikeCount(0);
        }
        return postRepository.save(post);
    }
     */
}
