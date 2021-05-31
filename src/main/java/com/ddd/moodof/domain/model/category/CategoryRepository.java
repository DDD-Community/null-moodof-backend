package com.ddd.moodof.domain.model.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByPreviousId(Long previousId);

    Optional<Category> findByIdAndUserId(Long id, Long userId);

    Optional<Category> findByUserIdAndPreviousIdAndIdNot(Long userId, Long previousId, Long id);

    Optional<Category> findByUserIdAndPreviousId(Long userId, Long previousId);

    long countByUserId(Long userId);
}
