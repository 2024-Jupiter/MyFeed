package com.myfeed.repository;

import com.myfeed.model.board.Board;
import com.myfeed.model.board.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByCategory(Tag tag, Pageable pageable);
}
