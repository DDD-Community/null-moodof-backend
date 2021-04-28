package com.ddd.moodof.domain.model.storage.photo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoragePhotoRepository extends JpaRepository<StoragePhoto, Long> {

    boolean existsByIdAndUserId(Long id, Long userId);

    Optional<StoragePhoto> findByIdAndUserId(Long id, Long userId);

}
