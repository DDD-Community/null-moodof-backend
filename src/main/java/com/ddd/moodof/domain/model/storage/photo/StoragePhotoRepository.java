package com.ddd.moodof.domain.model.storage.photo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoragePhotoRepository extends JpaRepository<StoragePhoto, Long> {

    boolean existsByIdAndUserId(Long id, Long userId);

    long countByUserId(Long userId);
}
