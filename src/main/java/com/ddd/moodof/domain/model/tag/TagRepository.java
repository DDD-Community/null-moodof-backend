package com.ddd.moodof.domain.model.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByUserId(Long userId);
    Optional<Tag> findTagByTagNameAndUserId(String TagName, Long userId);
    Optional<Tag> findTagByIdAndUserId(Long id, Long userId);
    Long deleteByIdAndUserId(Long tagName, Long UserId);
}
