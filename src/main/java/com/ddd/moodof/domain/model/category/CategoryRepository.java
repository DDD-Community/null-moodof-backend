package com.ddd.moodof.domain.model.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findOptionalByPreviousId(Long previousId);

    Optional<Category> findByIdAndUserId(Long id, Long userId);

    List<Category> findAllByUserId(Long userId);

    List<Category> findAllByPreviousId(Long previousId);
}
