package com.ddd.moodof.domain.model.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByPreviousBoardIdAndUserIdAndIdNot(Long previousBoardId, Long userId, Long id);

    boolean existsByIdAndUserId(Long id, Long userId);
}
