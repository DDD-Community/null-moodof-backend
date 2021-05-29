package com.ddd.moodof.domain.model.board.photo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardPhotoRepository extends JpaRepository<BoardPhoto, Long> {
    Optional<BoardPhoto> findFirstByBoardIdOrderByLastModifiedDateDesc(Long boardId);

    List<BoardPhoto> findAllByBoardIdAndUserId(Long boardId, Long userId);
}
