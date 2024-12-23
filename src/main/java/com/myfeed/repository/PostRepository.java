package com.myfeed.repository;

import com.myfeed.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserUid(long uid);
}
