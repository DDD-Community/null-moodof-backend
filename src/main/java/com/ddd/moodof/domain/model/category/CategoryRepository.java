package com.ddd.moodof.domain.model.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameAndUserId(String name, Long userId);

    List<Category> findAllByUserId(Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

}
