package com.myfeed.service.board;

import com.myfeed.model.board.Board;
import com.myfeed.model.board.Tag;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BoardService {
    public static final int PAGE_SIZE = 8;

    Page<Board> getPagedBoardByCategory(int page, Tag tag);
}
