package com.ddd.moodof.domain.model.storage.photo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoragePhotoRepository extends JpaRepository<StoragePhoto, Long> {

    boolean existsByIdInAndUserId(List<Long> ids, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    void deleteAllByIdIn(List<Long> ids);
}
