package com.ddd.moodof.domain.model.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByUserId(Long userId);

    Optional<Tag> findTagByNameAndUserId(String name, Long userId);

    Optional<Tag> findTagByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    long countByUserId(Long userId);
}
