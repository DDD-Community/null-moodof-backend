package com.ddd.moodof.domain.model.storage.photo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoragePhotoRepository extends JpaRepository<StoragePhoto, Long> {
    List<StoragePhoto> findPageByUserId(Long userId, Pageable pageable);

    Optional<StoragePhoto> findByIdAndUserId(Long id, Long userId);

}
