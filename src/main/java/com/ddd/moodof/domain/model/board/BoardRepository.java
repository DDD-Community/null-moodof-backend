package com.ddd.moodof.domain.model.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByPreviousBoardIdAndIdNot(Long previousBoardId, Long id);

    Optional<Board> findByPreviousBoardId(Long previousBoardId);

    Optional<Board> findByPreviousBoardIdAndCategoryId(Long previousBoardId, Long categoryId);

    Optional<Board> findBySharedKey(String sharedKey);

    void deleteAllByCategoryId(Long categoryId);

    boolean existsByIdAndUserId(Long id, Long userId);

    Optional<Board> findByUserIdAndPreviousBoardIdAndIdNot(Long userId, Long previousBoardId, Long id);

    Optional<Board> findByUserIdAndPreviousBoardId(Long userId, Long id);

    Optional<Board> findByIdAndUserId(Long id, Long userId);
}
