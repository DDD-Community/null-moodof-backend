package com.ddd.moodof.adapter.infrastructure.querydsl;

import com.ddd.moodof.application.dto.StoragePhotoDTO;
import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import com.ddd.moodof.domain.model.storage.photo.StoragePhotoQueryRepository;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public StoragePhotoDTO.StoragePhotoPageResponse findPageExcludeTrash(Long userId, int page, int size, String sortBy, boolean descending) {
        Pageable pageable = PageRequest.of(page, size, getSort(sortBy, descending));

        JPAQuery<StoragePhoto> storagePhotoJPAQuery = jpaQueryFactory.selectFrom(storagePhoto)
                .leftJoin(trashPhoto).on(storagePhoto.id.eq(trashPhoto.storagePhotoId))
                .where(storagePhoto.userId.eq(userId).and(trashPhoto.id.isNull()));

        List<StoragePhoto> storagePhotos = new Querydsl(em, new PathBuilderFactory().create(StoragePhoto.class))
                .applyPagination(pageable, storagePhotoJPAQuery)
                .fetch();

        return new StoragePhotoDTO.StoragePhotoPageResponse((long) Math.ceil((double) storagePhotoJPAQuery.fetchCount() / pageable.getPageSize()),
                StoragePhotoDTO.StoragePhotoResponse.listFrom(storagePhotos));

    }

    private Sort getSort(String sortBy, boolean descending) {
        Sort sort = Sort.by(sortBy);
        if (descending) {
            return sort.descending();
        }
        return sort.ascending();
    }
}
