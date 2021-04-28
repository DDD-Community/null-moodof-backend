package com.ddd.moodof.adapter.infrastructure.repository;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.application.dto.TrashPhotoDTO;
import com.ddd.moodof.domain.model.trash.photo.TrashPhoto;
import com.ddd.moodof.domain.model.trash.photo.TrashPhotoQueryRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ddd.moodof.domain.model.storage.photo.QStoragePhoto.storagePhoto;
import static com.ddd.moodof.domain.model.trash.photo.QTrashPhoto.trashPhoto;

@RequiredArgsConstructor
@Repository
public class TrashPhotoQueryRepositoryImpl implements TrashPhotoQueryRepository {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;
    private final PaginationUtils paginationUtils;

    @Override
    public TrashPhotoDTO.TrashPhotoPageResponse findPage(Long userId, int page, int size, String sortBy, boolean descending) {
        JPAQuery<TrashPhotoDTO.TrashPhotoResponse> jpaQuery = jpaQueryFactory.select(Projections.constructor(TrashPhotoDTO.TrashPhotoResponse.class,
                trashPhoto.id,
                Projections.constructor(StoragePhotoDTO.StoragePhotoResponse.class, storagePhoto.id,
                        storagePhoto.userId,
                        storagePhoto.uri,
                        storagePhoto.representativeColor,
                        storagePhoto.createdDate,
                        storagePhoto.lastModifiedDate),
                trashPhoto.userId,
                trashPhoto.createdDate,
                trashPhoto.lastModifiedDate
        )).from(trashPhoto)
                .leftJoin(storagePhoto).on(trashPhoto.storagePhotoId.eq(storagePhoto.id));

        List<TrashPhotoDTO.TrashPhotoResponse> trashPhotoResponses = new Querydsl(em, new PathBuilderFactory().create(TrashPhoto.class))
                .applyPagination(PageRequest.of(page, size, paginationUtils.getSort(sortBy, descending)), jpaQuery)
                .fetch();

        return new TrashPhotoDTO.TrashPhotoPageResponse(paginationUtils.getTotalPageCount(jpaQuery.fetchCount(), size), trashPhotoResponses);
    }
}
