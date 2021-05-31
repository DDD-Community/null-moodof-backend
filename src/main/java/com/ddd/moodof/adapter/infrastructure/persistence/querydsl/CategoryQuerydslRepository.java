package com.ddd.moodof.adapter.infrastructure.persistence.querydsl;

import com.ddd.moodof.application.dto.BoardDTO;
import com.ddd.moodof.application.dto.CategoryDTO;
import com.ddd.moodof.domain.model.category.CategoryQueryRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

import static com.ddd.moodof.domain.model.board.QBoard.board;
import static com.ddd.moodof.domain.model.category.QCategory.category;
import static com.querydsl.core.types.Projections.constructor;

@RequiredArgsConstructor
@Repository
public class CategoryQuerydslRepository implements CategoryQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CategoryDTO.CategoryWithBoardResponse> findAllByUserId(Long userId) {
        List<CategoryDTO.CategoryWithBoardResponse> categoryWithBoardList = new ArrayList<>();
        BooleanExpression includeUserId = includeUserId(userId);

        List<CategoryDTO.CategoryResponse> categories = jpaQueryFactory.select(
                constructor(CategoryDTO.CategoryResponse.class,
                        category.id,
                        category.userId,
                        category.title,
                        category.previousId,
                        category.createdDate,
                        category.lastModifiedDate))
                .from(category)
                .where(includeUserId)
                .fetch();

        for (CategoryDTO.CategoryResponse category : categories) {
            List<BoardDTO.BoardResponse> boards = jpaQueryFactory.select(
                    constructor(BoardDTO.BoardResponse.class,
                            board.id,
                            board.previousBoardId,
                            board.userId,
                            board.name,
                            board.categoryId,
                            board.sharedKey,
                            board.createdDate,
                            board.lastModifiedDate))
                    .from(board)
                    .where(board.userId.eq(category.getUserId()).and(board.categoryId.eq(category.getId())))
                    .fetch();
            categoryWithBoardList.add(CategoryDTO.CategoryWithBoardResponse.from(category, boards));
        }
        return categoryWithBoardList;
    }

    private BooleanExpression includeUserId(Long userId) {
        return category.userId.eq(userId);
    }
}
