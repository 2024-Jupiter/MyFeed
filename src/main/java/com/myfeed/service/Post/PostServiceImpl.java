package com.myfeed.service.Post;

import com.myfeed.converter.PostConverter;
import com.myfeed.model.post.Post;
import com.myfeed.model.post.PostCommentList;
import com.myfeed.model.post.PostEs;
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
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired PostRepository postRepository;
    @Autowired PostEsRepository postEsRepository;
    @Autowired UserRepository userRepository;

    @Transactional
    @Override
    public Post createPost(String uid, String category, String title, String content, String imgSrc) {
        User user = userRepository.findById(uid).orElse(null);
        Post post = Post.builder()
                .user(user).category(category).title(title).content(content).imgSrc(imgSrc)
                .createAt(LocalDateTime.now()).updateAt(LocalDateTime.now())
                .viewCount(0).likeCount(0)
                .build();

        Post savedPost = postRepository.save(post);
        PostEs postEs = PostConverter.toElasticsearchDocument(savedPost);
        postEsRepository.save(postEs);
        return savedPost;
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
    public List<Post> getPostsByUser(String uid) {
        return postRepository.findByUserUid(uid);
    }
}
