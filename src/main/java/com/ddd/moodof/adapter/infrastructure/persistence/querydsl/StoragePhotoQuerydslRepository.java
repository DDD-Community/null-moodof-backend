package com.ddd.moodof.adapter.infrastructure.persistence.querydsl;

import com.ddd.moodof.adapter.infrastructure.persistence.PaginationUtils;
import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoQueryRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

import static com.ddd.moodof.domain.model.storage.photo.QStoragePhoto.storagePhoto;
import static com.ddd.moodof.domain.model.tag.attachment.QTagAttachment.tagAttachment;
import static com.ddd.moodof.domain.model.trash.photo.QTrashPhoto.trashPhoto;

@RequiredArgsConstructor
@Repository
public class StoragePhotoQuerydslRepository implements StoragePhotoQueryRepository {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;
    private final PaginationUtils paginationUtils;

    @Override
    public StoragePhotoDTO.StoragePhotoPageResponse findPageExcludeTrash(Long userId, Pageable pageable, List<Long> tagIds) {

        JPAQuery<StoragePhotoDTO.StoragePhotoResponse> jpaQuery = getQuery(userId, tagIds);

        List<StoragePhotoDTO.StoragePhotoResponse> responses = new Querydsl(em, new PathBuilderFactory().create(StoragePhoto.class))
                .applyPagination(pageable, jpaQuery)
                .fetch();

        return new StoragePhotoDTO.StoragePhotoPageResponse(paginationUtils.getTotalPageCount(jpaQuery.fetchCount(), pageable.getPageSize()), responses);
    }

    private JPAQuery<StoragePhotoDTO.StoragePhotoResponse> getQuery(Long userId, List<Long> tagIds) {
        JPAQuery<StoragePhotoDTO.StoragePhotoResponse> storagePhotoQuery = jpaQueryFactory.select(Projections.constructor(StoragePhotoDTO.StoragePhotoResponse.class,
                storagePhoto.id,
                storagePhoto.userId,
                storagePhoto.uri,
                storagePhoto.representativeColor,
                storagePhoto.createdDate,
                storagePhoto.lastModifiedDate
        ))
                .from(storagePhoto)
                .leftJoin(trashPhoto).on(storagePhoto.id.eq(trashPhoto.storagePhotoId));

        BooleanExpression excludeTrash = storagePhoto.userId.eq(userId).and(trashPhoto.id.isNull());

        if (Objects.isNull(tagIds) || tagIds.isEmpty()) {
            return storagePhotoQuery
                    .where(excludeTrash);
        }
        return storagePhotoQuery
                .leftJoin(tagAttachment).on(storagePhoto.id.eq(tagAttachment.storagePhotoId))
                .where(excludeTrash.and(tagAttachment.tagId.in(tagIds)));
    }
}
