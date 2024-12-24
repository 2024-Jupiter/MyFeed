package com.myfeed.service.board;

import com.myfeed.model.board.Board;
import com.myfeed.model.board.Tag;
import com.myfeed.model.post.Post;
import com.myfeed.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    @Autowired private BoardRepository boardRepository;

    @Override
    public Page<Board> getPagedBoardByCategory(int page, Tag tag) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        return boardRepository.findByCategory(tag, pageable);
    }
}
