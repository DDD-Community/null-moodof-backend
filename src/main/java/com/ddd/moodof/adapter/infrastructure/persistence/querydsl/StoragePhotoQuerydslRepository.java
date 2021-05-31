package com.ddd.moodof.adapter.infrastructure.persistence.querydsl;

import com.ddd.moodof.adapter.infrastructure.persistence.PaginationUtils;
import com.ddd.moodof.application.dto.*;
import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoQueryRepository;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ddd.moodof.domain.model.board.QBoard.board;
import static com.ddd.moodof.domain.model.board.photo.QBoardPhoto.boardPhoto;
import static com.ddd.moodof.domain.model.category.QCategory.category;
import static com.ddd.moodof.domain.model.storage.photo.QStoragePhoto.storagePhoto;
import static com.ddd.moodof.domain.model.tag.QTag.tag;
import static com.ddd.moodof.domain.model.tag.attachment.QTagAttachment.tagAttachment;
import static com.ddd.moodof.domain.model.trash.photo.QTrashPhoto.trashPhoto;

@RequiredArgsConstructor
@Repository
public class StoragePhotoQuerydslRepository implements StoragePhotoQueryRepository {
    private static final long NO_TAG = 0L;

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;
    private final PaginationUtils paginationUtils;

    @Override
    public StoragePhotoDTO.StoragePhotoPageResponse findPageExcludeTrash(Long userId, Pageable pageable, List<Long> tagIds) {

        JPAQuery<StoragePhotoDTO.StoragePhotoResponse> jpaQuery = getQuery(userId, tagIds);

        List<StoragePhotoDTO.StoragePhotoResponse> responses = new Querydsl(em, new PathBuilderFactory().create(StoragePhoto.class))
                .applyPagination(pageable, jpaQuery)
                .fetch();

        long totalCount = jpaQuery.fetchCount();

        return new StoragePhotoDTO.StoragePhotoPageResponse(totalCount, paginationUtils.getTotalPageCount(totalCount, pageable.getPageSize()), responses);
    }

    private JPAQuery<StoragePhotoDTO.StoragePhotoResponse> getQuery(Long userId, List<Long> tagIds) {
        JPAQuery<StoragePhotoDTO.StoragePhotoResponse> storagePhotoQuery = jpaQueryFactory
                .select(Projections.constructor(StoragePhotoDTO.StoragePhotoResponse.class,
                        storagePhoto.id,
                        storagePhoto.userId,
                        storagePhoto.uri,
                        storagePhoto.representativeColor,
                        storagePhoto.createdDate,
                        storagePhoto.lastModifiedDate
                ))
                .from(storagePhoto)
                .leftJoin(trashPhoto).on(storagePhoto.id.eq(trashPhoto.storagePhotoId));

        BooleanExpression excludeTrash = excludeTrash(userId);

        if (Objects.isNull(tagIds) || tagIds.isEmpty()) {
            return storagePhotoQuery
                    .where(excludeTrash);
        }
        if (tagIds.contains(NO_TAG)) {
            return storagePhotoQuery
                    .leftJoin(tagAttachment).on(storagePhoto.id.eq(tagAttachment.storagePhotoId))
                    .where(excludeTrash.and(tagAttachment.tagId.in(tagIds).or(tagAttachment.id.isNull())));
        }
        return storagePhotoQuery
                .innerJoin(tagAttachment).on(storagePhoto.id.eq(tagAttachment.storagePhotoId))
                .where(excludeTrash.and(tagAttachment.tagId.in(tagIds)));
    }

    private BooleanExpression excludeTrash(Long userId) {
        return storagePhoto.userId.eq(userId).and(trashPhoto.id.isNull());
    }

    @Override
    public StoragePhotoDTO.StoragePhotoDetailResponse findDetail(Long userId, Long id, List<Long> tagIds) {
        StoragePhotoDTO.StoragePhotoDetailResponse storagePhotoDetailResponse = jpaQueryFactory
                .select(new QStoragePhotoDTO_StoragePhotoDetailResponse(
                        storagePhoto.id,
                        storagePhoto.userId,
                        storagePhoto.uri,
                        storagePhoto.representativeColor,
                        storagePhoto.createdDate,
                        storagePhoto.lastModifiedDate,
                        ExpressionUtils.as(previousStoragePhotoId(userId, id, tagIds), "previousStoragePhotoId"),
                        ExpressionUtils.as(nextStoragePhotoId(userId, id, tagIds), "nextStoragePhotoId")))
                .from(storagePhoto)
                .where(storagePhoto.id.eq(id))
                .fetchOne();

        storagePhotoDetailResponse.setCategories(findCategories(id));
        storagePhotoDetailResponse.setTags(findTags(id));

        return storagePhotoDetailResponse;
    }

    private JPQLQuery<Long> previousStoragePhotoId(Long userId, Long id, List<Long> tagIds) {
        return JPAExpressions
                .select(storagePhoto.id)
                .from(storagePhoto)
                .where(storagePhoto.userId.eq(userId).and(storagePhoto.lastModifiedDate.eq(lastModifiedDateAfterStoragePhoto(userId, id, tagIds))));
    }

    private JPQLQuery<LocalDateTime> lastModifiedDateAfterStoragePhoto(Long userId, Long id, List<Long> tagIds) {
        if (Objects.isNull(tagIds) || tagIds.isEmpty()) {
            return JPAExpressions
                    .select(storagePhoto.lastModifiedDate.min())
                    .from(storagePhoto)
                    .leftJoin(trashPhoto).on(storagePhoto.id.eq(trashPhoto.storagePhotoId))
                    .where(excludeTrash(userId).and(storagePhoto.lastModifiedDate.after(storagePhotoModifiedDate(id))));
        }
        if (tagIds.contains(NO_TAG)) {
            return JPAExpressions
                    .select(storagePhoto.lastModifiedDate.min())
                    .from(storagePhoto)
                    .leftJoin(trashPhoto).on(storagePhoto.id.eq(trashPhoto.storagePhotoId))
                    .leftJoin(tagAttachment).on(storagePhoto.id.eq(tagAttachment.storagePhotoId))
                    .where(excludeTrash(userId).and(tagAttachment.tagId.in(tagIds).or(tagAttachment.id.isNull())).and(storagePhoto.lastModifiedDate.after(storagePhotoModifiedDate(id))));
        }

        return JPAExpressions
                .select(storagePhoto.lastModifiedDate.min())
                .from(storagePhoto)
                .leftJoin(trashPhoto).on(storagePhoto.id.eq(trashPhoto.storagePhotoId))
                .innerJoin(tagAttachment).on(storagePhoto.id.eq(tagAttachment.storagePhotoId))
                .where(excludeTrash(userId).and(tagAttachment.tagId.in(tagIds)).and(storagePhoto.lastModifiedDate.after(storagePhotoModifiedDate(id))));
    }

    private JPQLQuery<LocalDateTime> storagePhotoModifiedDate(Long id) {
        return JPAExpressions
                .select(storagePhoto.lastModifiedDate)
                .from(storagePhoto)
                .where(storagePhoto.id.eq(id));
    }

    private JPQLQuery<Long> nextStoragePhotoId(Long userId, Long id, List<Long> tagIds) {
        return JPAExpressions
                .select(storagePhoto.id)
                .from(storagePhoto)
                .where(storagePhoto.userId.eq(userId).and(storagePhoto.lastModifiedDate.eq(lastModifiedDateBeforeStoragePhoto(userId, id, tagIds))));
    }

    private JPQLQuery<LocalDateTime> lastModifiedDateBeforeStoragePhoto(Long userId, Long id, List<Long> tagIds) {
        if (Objects.isNull(tagIds) || tagIds.isEmpty()) {
            return JPAExpressions
                    .select(storagePhoto.lastModifiedDate.max())
                    .from(storagePhoto)
                    .leftJoin(trashPhoto).on(storagePhoto.id.eq(trashPhoto.storagePhotoId))
                    .where(excludeTrash(userId).and(storagePhoto.lastModifiedDate.before(storagePhotoModifiedDate(id))));
        }
        if (tagIds.contains(NO_TAG)) {
            return JPAExpressions
                    .select(storagePhoto.lastModifiedDate.max())
                    .from(storagePhoto)
                    .leftJoin(trashPhoto).on(storagePhoto.id.eq(trashPhoto.storagePhotoId))
                    .leftJoin(tagAttachment).on(storagePhoto.id.eq(tagAttachment.storagePhotoId))
                    .where(excludeTrash(userId).and(tagAttachment.tagId.in(tagIds).or(tagAttachment.id.isNull())).and(storagePhoto.lastModifiedDate.before(storagePhotoModifiedDate(id))));
        }
        return JPAExpressions
                .select(storagePhoto.lastModifiedDate.max())
                .from(storagePhoto)
                .leftJoin(trashPhoto).on(storagePhoto.id.eq(trashPhoto.storagePhotoId))
                .innerJoin(tagAttachment).on(storagePhoto.id.eq(tagAttachment.storagePhotoId))
                .where(excludeTrash(userId).and(tagAttachment.tagId.in(tagIds)).and(storagePhoto.lastModifiedDate.before(storagePhotoModifiedDate(id))));
    }

    private List<CategoryDTO.CategoryDetailResponse> findCategories(Long storagePhotoId) {
        List<BoardDTO.BoardResponse> boards = findBoards(storagePhotoId);

        List<Long> boardIds = boards.stream()
                .map(BoardDTO.BoardResponse::getCategoryId)
                .collect(Collectors.toList());

        Map<Long, List<BoardDTO.BoardResponse>> boardsByCategoryId = boards.stream()
                .collect(Collectors.groupingBy(BoardDTO.BoardResponse::getCategoryId));

        List<CategoryDTO.CategoryDetailResponse> categories = jpaQueryFactory
                .select(new QCategoryDTO_CategoryDetailResponse(
                        category.id,
                        category.userId,
                        category.title,
                        category.previousId,
                        category.createdDate,
                        category.lastModifiedDate
                ))
                .from(category)
                .where(category.id.in(boardIds))
                .fetch();

        categories.forEach(it -> it.setBoards(boardsByCategoryId.get(it.getId())));
        return categories;
    }

    private List<BoardDTO.BoardResponse> findBoards(Long storagePhotoId) {
        return jpaQueryFactory
                .select(Projections.constructor(BoardDTO.BoardResponse.class,
                        board.id,
                        board.previousBoardId,
                        board.userId,
                        board.name,
                        board.categoryId,
                        board.sharedKey,
                        board.createdDate,
                        board.lastModifiedDate
                ))
                .from(board)
                .innerJoin(boardPhoto).on(board.id.eq(boardPhoto.boardId))
                .where(boardPhoto.storagePhotoId.eq(storagePhotoId))
                .fetch();
    }

    private List<TagDTO.TagResponse> findTags(Long id) {
        return jpaQueryFactory
                .select(Projections.constructor(TagDTO.TagResponse.class,
                        tag.id,
                        tag.userId,
                        tag.name,
                        tag.createdDate,
                        tag.lastModifiedDate
                ))
                .from(tag)
                .innerJoin(tagAttachment).on(tag.id.eq(tagAttachment.tagId))
                .where(tagAttachment.storagePhotoId.eq(id))
                .fetch();
    }
}
