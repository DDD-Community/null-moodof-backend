package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.TrashPhotoDTO;
import com.ddd.moodof.domain.model.trash.photo.TrashPhoto;
import com.ddd.moodof.domain.model.trash.photo.TrashPhotoQueryRepository;
import com.ddd.moodof.domain.model.trash.photo.TrashPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TrashPhotoService {
    private final TrashPhotoRepository trashPhotoRepository;
    private final TrashPhotoQueryRepository trashPhotoQueryRepository;
    private final StoragePhotoService storagePhotoService;

    public List<TrashPhotoDTO.TrashPhotoCreatedResponse> add(Long userId, TrashPhotoDTO.CreateTrashPhotos request) {
        if (request.getStoragePhotoId().stream().allMatch(it -> storagePhotoService.existsByIdAndUserId(it, userId))) {
            List<TrashPhoto> trashPhotos = trashPhotoRepository.saveAll(request.toEntities(userId));
            return TrashPhotoDTO.TrashPhotoCreatedResponse.listOf(trashPhotos);
        }
        throw new IllegalArgumentException("요청과 일치하지 않는 보관함사진이 존재합니다." + request.getStoragePhotoId());
    }

    public TrashPhotoDTO.TrashPhotoPageResponse findPage(Long userId, int page, int size, String sortBy, boolean descending) {
        return trashPhotoQueryRepository.findPage(userId, page, size, sortBy, descending);
    }

    public void cancel(Long id, Long userId) {
        if (!trashPhotoRepository.existsByIdAndUserId(id, userId)) {
            throw new IllegalArgumentException("요청과 일치하는 휴지통 사진이 없습니다. id / userId: " + id + " / " + userId);
        }
        trashPhotoRepository.deleteById(id);
    }
}
