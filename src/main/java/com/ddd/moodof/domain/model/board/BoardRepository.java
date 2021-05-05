package com.ddd.moodof.domain.model.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByPreviousBoardIdAndUserId(Long previousBoardId, Long userId);
}
