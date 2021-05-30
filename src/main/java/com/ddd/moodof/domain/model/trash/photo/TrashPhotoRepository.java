package com.ddd.moodof.domain.model.trash.photo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrashPhotoRepository extends JpaRepository<TrashPhoto, Long> {
    boolean existsByIdInAndUserId(List<Long> id, Long userId);

    void deleteAllByIdIn(List<Long> id);
}
