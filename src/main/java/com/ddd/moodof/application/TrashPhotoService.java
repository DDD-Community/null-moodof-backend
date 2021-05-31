package com.ddd.moodof.application;

import com.ddd.moodof.adapter.infrastructure.persistence.PaginationUtils;
import com.ddd.moodof.application.dto.TrashPhotoDTO;
import com.ddd.moodof.domain.model.trash.photo.TrashPhoto;
import com.ddd.moodof.domain.model.trash.photo.TrashPhotoQueryRepository;
import com.ddd.moodof.domain.model.trash.photo.TrashPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TrashPhotoService {
    private static final int MAX_TRASH_PHOTO_COUNT = 120;

    private final TrashPhotoRepository trashPhotoRepository;
    private final TrashPhotoQueryRepository trashPhotoQueryRepository;
    private final StoragePhotoService storagePhotoService;
    private final PaginationUtils paginationUtils;

    public List<TrashPhotoDTO.TrashPhotoCreatedResponse> add(Long userId, TrashPhotoDTO.CreateTrashPhotos request) {
        if (trashPhotoRepository.countByUserId(userId) >= MAX_TRASH_PHOTO_COUNT) {
            throw new IllegalStateException("휴지통 최대 개수는 120개 입니다.");
        }

        if (request.getStoragePhotoIds().stream().allMatch(it -> storagePhotoService.existsByIdAndUserId(it, userId))) {
            List<TrashPhoto> trashPhotos = trashPhotoRepository.saveAll(request.toEntities(userId));
            return TrashPhotoDTO.TrashPhotoCreatedResponse.listOf(trashPhotos);
        }
        throw new IllegalArgumentException("요청과 일치하지 않는 보관함사진이 존재합니다." + request.getStoragePhotoIds());
    }

    public TrashPhotoDTO.TrashPhotoPageResponse findPage(Long userId, int page, int size, String sortBy, boolean descending) {
        return trashPhotoQueryRepository.findPage(userId, PageRequest.of(page, size, paginationUtils.getSort(sortBy, descending)));
    }

    @Transactional
    public void cancel(TrashPhotoDTO.CancelTrashPhotos request, Long userId) {
        if (!trashPhotoRepository.existsByIdInAndUserId(request.getTrashPhotoIds(), userId)) {
            throw new IllegalArgumentException("요청과 일치하는 휴지통 사진이 없습니다.");
        }
        trashPhotoRepository.deleteAllByIdIn(request.getTrashPhotoIds());
    }
}
