package com.ddd.moodof.adapter.infrastructure.persistence.querydsl;

import com.ddd.moodof.domain.model.category.CategoryQueryRepository;
import com.ddd.moodof.domain.model.category.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class CategoryQuerydslRepository implements CategoryQueryRepository {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

}
