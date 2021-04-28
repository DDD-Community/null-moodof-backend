package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.TrashPhotoDTO;
import com.ddd.moodof.domain.model.storage.photo.StoragePhoto;
import com.ddd.moodof.domain.model.trash.photo.TrashPhoto;
import com.ddd.moodof.domain.model.trash.photo.TrashPhotoQueryRepository;
import com.ddd.moodof.domain.model.trash.photo.TrashPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TrashPhotoService {
    private final TrashPhotoRepository trashPhotoRepository;
    private final TrashPhotoQueryRepository trashPhotoQueryRepository;
    private final StoragePhotoService storagePhotoService;

    public TrashPhotoDTO.TrashPhotoResponse add(Long userId, TrashPhotoDTO.CreateTrashPhoto request) {
        StoragePhoto storagePhoto = storagePhotoService.findByIdAndUserId(request.getStoragePhotoId(), userId);
        TrashPhoto saved = trashPhotoRepository.save(request.toEntity(userId));
        return TrashPhotoDTO.TrashPhotoResponse.of(saved, storagePhoto);
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
