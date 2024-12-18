package com.myfeed.service.Post;

import com.myfeed.model.Post.Post;
import com.myfeed.model.Post.PostCommentList;
import com.myfeed.model.user.User;
import com.myfeed.repository.PostRepository;
import com.myfeed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired PostRepository postRepository;
    @Autowired UserRepository userRepository;


    @Override
    public Post createPost(String uid, List<PostCommentList> postCommentList, String category,
                           String title, String content, String imgSrc, LocalDateTime createAt,
                           LocalDateTime updateAt, int viewCount, int likeCount) {
        User user = userRepository.findById(uid);
        if (user != null) {
            Post post = Post.builder()
                    .postCommentLists(postCommentList).category(category).title(title).content(content)
                    .imgSrc(imgSrc).createAt(LocalDateTime.now()).updateAt(LocalDateTime.now()).viewCount(viewCount).likeCount(likeCount)
                    .build();
            return postRepository.save(post);
        } else {
            throw new RuntimeException("User or Book not found.");
        }
        return null;
    }

    @Override
    public void updatePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void deletePost(long pid) {
        postRepository.deleteById(pid);
    }

    @Override
    public List<Post> getPostsByUser(String uid) {
        return postRepository.findByUserUid(uid);
    }

    @Override
    public List<Post> getPostsByDateRange(LocalDateTime start, LocalDateTime end) {
        return postRepository.findByPostDateTimeBetween(start, end);
    }
}
