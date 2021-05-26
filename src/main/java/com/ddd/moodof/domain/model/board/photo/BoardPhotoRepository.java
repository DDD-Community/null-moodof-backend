package com.ddd.moodof.domain.model.board.photo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardPhotoRepository extends JpaRepository<BoardPhoto, Long> {
    long countByBoardId(Long boardId);
}
