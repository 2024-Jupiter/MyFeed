package com.myfeed.service.Post;

import com.myfeed.converter.PostConverter;
import com.myfeed.model.post.Category;
import com.myfeed.model.post.Image;
import com.myfeed.model.post.Post;
import com.myfeed.model.post.PostEs;
import com.myfeed.model.user.Role;
import com.myfeed.model.user.User;
import com.myfeed.repository.PostEsRepository;
import com.myfeed.repository.PostRepository;
import com.myfeed.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostServiceImpl implements PostService {
    @Autowired PostRepository postRepository;
    @Autowired PostEsRepository postEsRepository;
    @Autowired UserRepository userRepository;

    @Override
    public Post findByPid(long pid) {
        return postRepository.findById(pid).orElse(null);
    }

    @Transactional
    @Override
    public Post createPost(long uid, Category category, String title, String content, String imgSrc) {
        User user = userRepository.findById(uid).orElse(null);

        if (user.getRole().equals(Role.ADMIN)) {
            category = Category.NEWS;
            Post post = Post.builder()
                    .user(user).category(category).title(title).content(content)
                    .createAt(LocalDateTime.now())
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
                    .createAt(LocalDateTime.now())
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

    @Override
    public Page<Post> getMyPostList(int page, long uid) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return postRepository.findByUserUid(uid, pageable);
    }

    @Transactional
    @Override
    public void updatePost(Post post) {
        Post updatedPost = postRepository.save(post);
        PostEs postEs = PostConverter.toElasticsearchDocument(updatedPost);
        postEsRepository.save(postEs);
    }

    @Transactional
    @Override
    public void deletePost(long pid) {
        postRepository.deleteById(pid);
        postEsRepository.deleteById(String.valueOf(pid));
    }

    @Override
    public void incrementViewCount(long pid) {
        postRepository.incrementViewCount(pid);
    }

    @Override
    public void incrementLikeCount(long pid) {
        postRepository.incrementLikeCount(pid);
    }

    @Override
    public void decrementLikeCount(long pid) {
        postRepository.decrementLikeCount(pid);
    }
}
