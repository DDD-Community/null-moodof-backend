package com.ddd.moodof.adapter.infrastructure.repository;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoQueryRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ddd.moodof.domain.model.storage.photo.QStoragePhoto.storagePhoto;
import static com.ddd.moodof.domain.model.trash.photo.QTrashPhoto.trashPhoto;

@RequiredArgsConstructor
@Repository
public class StoragePhotoQueryRepositoryImpl implements StoragePhotoQueryRepository {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;
    private final PaginationUtils paginationUtils;

    @Override
    public StoragePhotoDTO.StoragePhotoPageResponse findPageExcludeTrash(Long userId, Pageable pageable) {

        JPAQuery<StoragePhotoDTO.StoragePhotoResponse> jpaQuery = jpaQueryFactory.select(Projections.constructor(StoragePhotoDTO.StoragePhotoResponse.class,
                storagePhoto.id,
                storagePhoto.userId,
                storagePhoto.uri,
                storagePhoto.representativeColor,
                storagePhoto.createdDate,
                storagePhoto.lastModifiedDate
        ))
                .from(storagePhoto)
                .leftJoin(trashPhoto).on(storagePhoto.id.eq(trashPhoto.storagePhotoId))
                .where(storagePhoto.userId.eq(userId).and(trashPhoto.id.isNull()));

        List<StoragePhotoDTO.StoragePhotoResponse> responses = new Querydsl(em, new PathBuilderFactory().create(StoragePhoto.class))
                .applyPagination(pageable, jpaQuery)
                .fetch();

        return new StoragePhotoDTO.StoragePhotoPageResponse(paginationUtils.getTotalPageCount(jpaQuery.fetchCount(), pageable.getPageSize()), responses);
    }
}
