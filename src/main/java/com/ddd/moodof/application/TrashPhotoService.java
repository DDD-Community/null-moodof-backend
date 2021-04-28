package com.ddd.moodof.application;

import com.ddd.moodof.application.dto.TrashPhotoDTO;
import com.ddd.moodof.domain.model.trash.photo.TrashPhoto;
import com.ddd.moodof.domain.model.trash.photo.TrashPhotoRepository;
import com.ddd.moodof.domain.model.trash.photo.TrashPhotoVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TrashPhotoService {
    private final TrashPhotoRepository trashPhotoRepository;
    private final TrashPhotoVerifier trashPhotoVerifier;

    public TrashPhotoDTO.TrashPhotoResponse add(Long userId, TrashPhotoDTO.CreateTrashPhoto request) {
        TrashPhoto saved = trashPhotoRepository.save(trashPhotoVerifier.toEntity(userId, request.getStoragePhotoId()));
        return TrashPhotoDTO.TrashPhotoResponse.from(saved);
    }

    public void cancel(Long id, Long userId) {
        if (!trashPhotoRepository.existsByIdAndUserId(id, userId)) {
            throw new IllegalArgumentException("요청과 일치하는 휴지통 사진이 없습니다. id / userId: " + id + " / " + userId);
        }
        trashPhotoRepository.deleteById(id);
    }
}
