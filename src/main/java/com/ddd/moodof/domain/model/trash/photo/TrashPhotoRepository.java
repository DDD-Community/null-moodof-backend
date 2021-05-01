package com.ddd.moodof.domain.model.trash.photo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashPhotoRepository extends JpaRepository<TrashPhoto, Long> {
    boolean existsByIdAndUserId(Long id, Long userId);
}
